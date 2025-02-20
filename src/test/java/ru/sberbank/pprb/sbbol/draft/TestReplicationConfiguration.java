package ru.sberbank.pprb.sbbol.draft;

import com.sbt.pprb.integration.tests.common.mock.JournalClientMock;
import liquibase.integration.spring.SpringLiquibase;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import ru.sbrf.journal.client.JournalCreatorClientApi;

import javax.sql.DataSource;

/**
 * Конфигурация для тестирования механизма репликации между основной и StandIn базой
 */
@TestConfiguration
@EnableConfigurationProperties(LiquibaseProperties.class)
public class TestReplicationConfiguration {

    /**
     * Платформенный mock-бин, осуществляющий репликацию между базами
     */
    @Bean
    @Primary
    public JournalCreatorClientApi journalCreatorClientApi() {
        JournalClientMock journalClientMock = new JournalClientMock();
        journalClientMock.setEnableAutoReplication(true);
        return journalClientMock;
    }

    /**
     * Конфигурация для выполнения liquibase на основной БД
     */
    @Bean
    public SpringLiquibase primaryLiquibase(@Qualifier("mainDataSource") DataSource mainDataSource,
                                            LiquibaseProperties properties) {
        return liquibaseBean(mainDataSource, properties);
    }

    /**
     * Конфигурация для выполнения liquibase на StandIn БД
     */
    @Bean
    public SpringLiquibase secondaryLiquibase(@Qualifier("standInDataSource") DataSource standInDataSource,
                                              LiquibaseProperties properties) {
        return liquibaseBean(standInDataSource, properties);
    }

    /**
     * Общая конфигурация liquibase
     */
    @NotNull
    private SpringLiquibase liquibaseBean(DataSource ds, LiquibaseProperties properties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(ds);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        liquibase.setLabels(properties.getLabels());
        liquibase.setChangeLogParameters(properties.getParameters());
        liquibase.setRollbackFile(properties.getRollbackFile());
        return liquibase;
    }
}
