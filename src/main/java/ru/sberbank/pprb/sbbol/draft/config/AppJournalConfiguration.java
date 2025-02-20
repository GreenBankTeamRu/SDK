package ru.sberbank.pprb.sbbol.draft.config;

import com.sbt.pprb.integration.hibernate.standin.StandinPlugin;
import com.sbt.pprb.integration.replication.hashkey.InterfaceBasedHashKeyResolver;
import com.sbt.pprb.integration.replication.transport.JournalSubscriptionImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import ru.sbrf.journal.client.JournalCreatorClientApi;
import ru.sbrf.journal.standin.StandinConfiguration;
import ru.sbrf.journal.standin.StandinResourceHelper;
import ru.sbrf.journal.standin.consumer.api.SubscriptionService;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Конфигурация прикладного журнала. Обеспечивает отправку в ПЖ векторов изменений и их применение на другую БД
 */
@Configuration
@Import({StandinConfiguration.class})
@EnableConfigurationProperties({StandInPluginConfig.class})
public class AppJournalConfiguration {

    /**
     * Бин, подписывающийся на сообщения прикладного журнала и выполняющий применение векторов изменений на
     * второй базе данных
     *
     * @param zoneId идентификатор зоны в ПЖ для фабрики
     * @return сервис, реализующий подписку на прикладной журнал
     */
    @Bean
    public SubscriptionService subscriptionService(@Value("${standin.cloud.client.zoneId}") String zoneId) {
        return new JournalSubscriptionImpl(zoneId);
    }

    /**
     * Бин, обеспечивающий отправку векторов изменений в прикладной журнал при выполнении транзакции на основной БД
     *
     * @param moduleId             идентификатор модуля, по которому фабрика зарегистрирована в ПЖ
     * @param masterDataSource     основной датасорс
     * @param standinDataSource    датасорс базы SI
     * @param entityManagerFactory фабрика EntityManager
     * @param journalClient        клиент для работы с Kafka ПЖ
     * @return плагин с конфигурацией для отправки векторов в ПЖ
     */
    @Bean
    public StandinPlugin standinPlugin(
            @Value("${appjournal.moduleId}") String moduleId,
            @Qualifier("mainDataSource") DataSource masterDataSource,
            @Qualifier("standInDataSource") DataSource standinDataSource,
            EntityManagerFactory entityManagerFactory,
            JournalCreatorClientApi journalClient,
            StandInPluginConfig standInPluginConfig
    ) {
        StandinPlugin.Configurator configurator = StandinPlugin.configurator(entityManagerFactory);
        configurator.setMasterDataSource(masterDataSource);
        configurator.setStandinDataSource(standinDataSource);
        configurator.setJournalClient(journalClient);

        // Реализация HashKey функции.
        configurator.setJournalHashKeyResolver(new InterfaceBasedHashKeyResolver());
        // Идентификатор модуля
        configurator.setModuleIdProvider(() -> moduleId);
        // Стратегия репликации
        configurator.setReplicationStrategy(standInPluginConfig.getReplicationStrategy());
        // Тип сериализатора
        configurator.setSerializerType(standInPluginConfig.getSerializerType());
        // Режим предварительной блокировки записи в служебной таблице
        configurator.setPartitionLockMode(standInPluginConfig.getPartitionLockMode());
        // Стратегия контроля порядка применения векторов
        configurator.setOrderingControlStrategy(standInPluginConfig.getOrderingControlStrategy());
        // Стратегия работы с несколькими hashKey в одной транзакции.
        configurator.setPartitionMultiplyingMode(standInPluginConfig.getPartitionMultiplyingMode());

        return configurator.configure();
    }

    @Bean
    @Primary
    public StandinResourceHelper<String> standinResourceHelper() {
        return new StandinResourceHelper<>("main", "standin");
    }

}
