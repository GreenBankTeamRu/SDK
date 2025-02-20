package ru.sberbank.pprb.sbbol.draft.db;

import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.TestcontainersConfiguration;

/**
 * Наследник контейнера PostgreSQL с возможностью переопределения docker registry для скачивания образа.
 * Ссылка на registry указывается через файл testcontainers.properties.
 */
public class ExternalRegistryPostgreSQLContainer<SELF extends ExternalRegistryPostgreSQLContainer<SELF>> extends PostgreSQLContainer<SELF> {

    public ExternalRegistryPostgreSQLContainer(String dockerImageName) {
        super(DockerImageName.parse(getRegistryUrl() + dockerImageName).asCompatibleSubstituteFor("postgres"));
    }

    private static String getRegistryUrl() {
        // registry указывается в файле testcontainers.properties
        String registryUrl = TestcontainersConfiguration.getInstance().getEnvVarOrProperty("docker.registry.url", null);
        if (StringUtils.isEmpty(registryUrl)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.appendIfMissing(registryUrl, "/");
    }

    @Override
    public void start() {
        super.start();
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger());
        followOutput(logConsumer); // отбрасывание логов контейнеров в стандартный вывод
    }
}
