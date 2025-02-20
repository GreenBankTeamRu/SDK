package ru.sberbank.pprb.sbbol.draft.db;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainerProvider;

/**
 * Расширение для testcontainers. Позволяет при указании "pg" в jdcb-url поднимать контейнер Postgres, загруженный
 * через указанный в файле настроек registry.
 *
 * Пример jdbc-url: jdbc:tc:pg:13-alpine:///main
 *
 * @see ExternalRegistryPostgreSQLContainer
 */
public class ExternalRegistryPostgreSQLContainerProvider extends PostgreSQLContainerProvider {

    private static final String CONTAINER_NAME = "ci02281165/ci03069386/postgres";

    private static final String NAME = "pg";

    @Override
    public boolean supports(String databaseType) {
        return NAME.equals(databaseType);
    }

    @Override
    public JdbcDatabaseContainer<?> newInstance(String tag) {
        return new ExternalRegistryPostgreSQLContainer<>(CONTAINER_NAME + ":" + tag);
    }

}
