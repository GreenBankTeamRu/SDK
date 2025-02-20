package ru.sberbank.pprb.sbbol.draft.changevector.generate;

import com.sbt.pprb.integration.changevector.ChangeSet;
import com.sbt.pprb.integration.changevector.ChangeVector;
import com.sbt.pprb.integration.changevector.building.CreateEventImplementor;
import com.sbt.pprb.integration.changevector.serialization.Serializer;
import com.sbt.pprb.integration.changevector.serialization.evo.ChangeVectorSerializer;
import com.sbt.pprb.integration.changevector.snapshot.ChangeSetImpl;
import com.sbt.pprb.integration.changevector.snapshot.ChangeVectorImpl;
import com.sbt.pprb.integration.changevector.snapshot.CreateEventImpl;
import com.sbt.pprb.integration.datafabric.hibernate.helptable.InitDataPartition;
import com.sbt.pprb.integration.hibernate.changes.serialization.meta.HibernateMetadataSource;
import com.sbt.pprb.integration.hibernate.changes.transform.Normalizer;
import com.sbt.pprb.integration.hibernate.standin.annotations.Replication;
import com.sbt.pprb.integration.hibernate.standin.annotations.Standin;
import com.sbt.pprb.integration.replication.clientlocks.model.ClientLock;
import com.sbt.pprb.integration.replication.clientlocks.model.ClientLockEvent;
import com.sbt.pprb.integration.replication.clientlocks.model.ConfirmationEntity;
import com.sbt.pprb.integration.replication.clientlocks.model.PartitionStateImpl;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.BasicType;
import org.hibernate.type.Type;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.ReflectionUtils;
import ru.dcbqa.allureee.annotations.layers.UnitTestLayer;
import ru.sberbank.pprb.sbbol.draft.HibernatePluginCleanerInitializer;
import ru.sberbank.pprb.sbbol.draft.TestReplicationConfiguration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static com.sbt.pprb.integration.hibernate.changes.transform.PropertyAccessor.getPropertyValue;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@ActiveProfiles("cv")
@Import(TestReplicationConfiguration.class)
@ContextConfiguration(initializers = {HibernatePluginCleanerInitializer.class})
@UnitTestLayer
public class ChangeVectorTest {

    private static final EasyRandom EASY_RANDOM = new EasyRandom();

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private SessionFactoryImplementor sessionFactory;

    @Test
    public void generateVectorsTest() throws Exception {
        Path path = Paths.get("vectors");
        path.toFile().mkdirs();

        Set<EntityType<?>> entities = emf.getMetamodel().getEntities();

        Normalizer normalizer = Normalizer.create(sessionFactory);

        HibernateMetadataSource metadataSource = new HibernateMetadataSource(sessionFactory);
        Serializer<ChangeVector> serializer = new ChangeVectorSerializer(metadataSource);

        for (EntityType<?> entityType : entities) {
            Class<?> javaType = entityType.getJavaType();
            // Не генерировать вектора по объектам, выключенным из репликации в Standin
            Standin standin = AnnotationUtils.findAnnotation(javaType, Standin.class);
            if (standin != null && standin.replication() == Replication.DISABLED) {
                continue;
            }
            if (!ClientLockEvent.class.equals(javaType) && !ClientLock.class.equals(javaType) &&
                !ConfirmationEntity.class.equals(javaType) && !PartitionStateImpl.class.equals(javaType) &&
                !InitDataPartition.class.equals(javaType)) {
                // generate
                Object entity = EASY_RANDOM.nextObject(javaType);
                String vector = createChangeVector(entity, normalizer, serializer);

                // запись вектора на ФС
                Files.writeString(path.resolve(javaType.getCanonicalName()), vector);
            }
        }
    }

    private String createChangeVector(Object entity, Normalizer normalizer, Serializer<ChangeVector> serializer) throws Exception {
        ChangeVectorImpl changeVector = new ChangeVectorImpl();
        changeVector.setTxId(UUID.randomUUID());
        changeVector.setPartitionId("partiion_id");

        EntityPersister persister = sessionFactory.getMetamodel().entityPersister(entity.getClass());
        String alias = persister.getEntityName();
        Field idField = getIdField(entity, persister);
        idField.setAccessible(true);
        Object idValue = idField.get(entity);
        Serializable id = normalizer.normalize(persister.getIdentifierType(), idValue);
        Serializable version = (Serializable) persister.getVersion(entity);

        CreateEventImplementor<?> createEvent = new CreateEventImpl(alias, id, version);
        fillEvent(entity, persister, normalizer, createEvent);

        ChangeSet changeSet = new ChangeSetImpl(Collections.singleton(createEvent), Collections.emptySet(), Collections.emptySet());
        changeVector.getChangeSets().add(changeSet);

        return serializer.serialize(changeVector);
    }

    private Field getIdField(Object entity, EntityPersister persister) {
        String identifierPropertyName = persister.getIdentifierPropertyName();
        Class<?> cls = entity.getClass();
        Field idField = null;
        while (idField == null && cls != null) {
            try {
                idField = cls.getDeclaredField(identifierPropertyName);
            } catch (NoSuchFieldException exception) {
                cls = cls.getSuperclass();
            }
        }
        if (idField == null) {
            throw new IllegalStateException("Cannot find field " + identifierPropertyName + " in class " + entity.getClass());
        }
        return idField;
    }

    /**
     * @see {@link com.sbt.pprb.integration.hibernate.changes.processors.CreateEventProcessor#modelToStore}
     */
    private void fillEvent(Object entity, EntityPersister persister, Normalizer normalizer, CreateEventImplementor<?> createEvent) {
        Type[] types = persister.getPropertyTypes();
        String[] propertyNames = persister.getPropertyNames();
        for (int i = 0; i < propertyNames.length; i++) {
            String name = propertyNames[i];
            if (!persister.getEntityMetamodel().getProperties()[i].isInsertable()) {
                continue;
            }
            if (isVersion(persister, name)) {
                continue;
            }
            if (isStandinReplicationDisabled(entity.getClass(), name)) {
                continue;
            }
            Type type = types[i];
            Object value = getPropertyValue(persister, entity, propertyNames[i]);
            writeProperty(createEvent, type, name, value, normalizer);
        }
    }

    private void writeProperty(
        CreateEventImplementor<?> createEvent,
        Type type,
        String propertyName,
        Object value,
        Normalizer normalizer
    ) {
        if (type.isCollectionType()) {
            return;
        }
        if (isEmpty(propertyName)) {
            throw new RuntimeException(format("Unable to detect property name of type %s for entity: \"%s\"",
                type.getName(),
                createEvent.getAlias()));
        }
        if (type.isEntityType()) {
            createEvent.addReference(propertyName, normalizer.normalize(type, value));
        } else if (type instanceof BasicType || type.isComponentType()) {
            createEvent.addPrimitive(propertyName, normalizer.normalize(type, value));
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private boolean isVersion(EntityPersister persister, String propertyName) {
        int versionProperty = persister.getVersionProperty();
        if (versionProperty < 0) {
            return false;
        }
        String versionPropertyName = persister.getPropertyNames()[versionProperty];
        return propertyName.equals(versionPropertyName);
    }

    /**
     * Вернуть true, если поле выключено из репликации в стендин.
     * <p>
     * Standin-плагин (sbp-hibernate-standin) не записывает в вектора изменений поля,
     * отмеченные аннотацией {@code @Standin(replication = Replication.DISABLED)},
     * поэтому такие поля не должны попадать и в вектора изменений, которые создает
     * текущий тестовый класс.
     *
     * @param entityClass класс @Entity-объекта
     * @param name        имя поля
     * @return true, если поле не должно попасть в вектор изменений
     */
    private boolean isStandinReplicationDisabled(Class<?> entityClass, String name) {
        Field field = ReflectionUtils.findField(entityClass, name);
        if (field != null) {
            Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnotations) {
                if (annotation instanceof Standin) {
                    Standin standin = (Standin) annotation;
                    return standin.replication() == Replication.DISABLED;
                }
            }
        }
        Method getterMethod = ReflectionUtils.findMethod(entityClass, getGetterName(name));
        if (getterMethod != null) {
            Standin standin = getterMethod.getAnnotation(Standin.class);
            if (standin != null) {
                return standin.replication() == Replication.DISABLED;
            }
        }
        return false;
    }

    private static String getGetterName(String propertyName) {
        return "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

}
