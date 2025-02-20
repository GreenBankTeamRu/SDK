package ru.sberbank.pprb.sbbol.draft;

import com.sbt.pprb.integration.hibernate.adapter.HibernateAdapter;
import com.sbt.pprb.integration.hibernate.adapter.HibernatePluginRegistrySpi;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Добавление зачистки плагинов Hibernate перед инициализацией контекста. Необходим для возможности запуска тестов
 * с разными контекстами.
 * Если не подключать очистку контекстов, то будет ошибка `DataType ORM_CV already configured`
 * {@link com.sbt.pprb.integration.hibernate.standin.plugin.StandinPluginImpl}
 */
public class HibernatePluginCleanerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ((HibernatePluginRegistrySpi) HibernateAdapter.getInstance().getPluginRegistry()).clean();
    }

}
