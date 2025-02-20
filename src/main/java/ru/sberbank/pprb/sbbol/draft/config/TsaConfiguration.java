package ru.sberbank.pprb.sbbol.draft.config;

import com.sbt.pprb.integration.datafabric.InitDataSampleFunctions;
import com.sbt.pprb.integration.datafabric.QualityDataSampleFunctions;
import com.sbt.pprb.integration.datafabric.api.SourceSystemDataProvider;
import com.sbt.pprb.integration.datafabric.hibernate.HibernateDataProviderFactory;
import com.sbt.pprbod.data.kafka.KafkaInitDataSampleService;
import com.sbt.pprbod.data.kafka.KafkaQualityDataSampleService;
import com.sbt.pprbod.data.monitoring.TsaMonitoringService;
import com.sbt.pprbod.data.monitoring.micrometer.MicrometerMonitoringService;
import com.sbt.pprbod.data.transport.init.InitDataSampleLoad;
import com.sbt.pprbod.data.transport.tkd.QualityDataSampleLoad;
import com.sbt.pprbod.exchange.spring.ClientConfiguration;
import com.sbt.pprbod.exchange.spring.config.PprbodCloudConfig;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManagerFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
@ConditionalOnProperty(
        name = {"pprbod.cloud.stub.enabled"},
        havingValue = "false",
        matchIfMissing = true
)
@Import(ClientConfiguration.class)
public class TsaConfiguration {
    private static final String SOURCE_MNEMONIC = "sber-business-api-sdk";

    @Value("${pprbod.cloud.tsaConfig.partitionSize:250}")
    private int partitionSize;
    @Value("${pprbod.cloud.tsaConfig.partitioningStrategy:COMPOSITE_OR_SIMPLE_ID_AUTOSELECT}")
    private HibernateDataProviderFactory.PartitioningStrategyType partitioningStrategy;
    @Value("${pprbod.cloud.tsaConfig.threadPoolSize:4}")
    private int threadPoolSize;

    @Bean
    public SourceSystemDataProvider dataProvider(EntityManagerFactory entityManagerFactory) {
        return new HibernateDataProviderFactory()
                .setEntityManagerFactory(entityManagerFactory)
                .setPartitioningStrategy(partitioningStrategy)
                .setPartitionSize(partitionSize)
                .build();
    }

    @Bean
    public KafkaQualityDataSampleService kafkaQualityDataSampleService(
            PprbodCloudConfig pprbodCloudConfig,
            SourceSystemDataProvider sourceSystemDataProvider,
            QualityDataSampleLoad qualityDataSampleLoad,
            TsaMonitoringService tsaMonitoringService
    ) {
        QualityDataSampleFunctions qualityFunctions = new QualityDataSampleFunctions(sourceSystemDataProvider);

        return new KafkaQualityDataSampleService(
                pprbodCloudConfig.getZone(),
                qualityDataSampleLoad,
                qualityFunctions::batchEstimate,
                qualityFunctions::loadKeys,
                qualityFunctions::loadData,
                tsaMonitoringService,
                SOURCE_MNEMONIC);
    }

    @Bean
    public KafkaInitDataSampleService kafkaInitDataSampleService(
            PprbodCloudConfig pprbodCloudConfig,
            SourceSystemDataProvider sourceSystemDataProvider,
            InitDataSampleLoad initDataSampleLoad,
            TsaMonitoringService tsaMonitoringService
    ) {
        final InitDataSampleFunctions initDataSampleFunctions = new InitDataSampleFunctions(sourceSystemDataProvider);

        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicLong THREAD_COUNTER = new AtomicLong(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("data-result-sender-" + THREAD_COUNTER.getAndIncrement());
                return thread;
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize, threadFactory);

        return new KafkaInitDataSampleService(
                pprbodCloudConfig.getZone(),
                executorService,
                initDataSampleLoad,
                initDataSampleFunctions::initLoad,
                initDataSampleFunctions::getBatchCount,
                initDataSampleFunctions::loadBatchAsync,
                initDataSampleFunctions::abort,
                tsaMonitoringService,
                SOURCE_MNEMONIC);
    }

    @Bean
    public TsaMonitoringService tsaMonitoringService(MeterRegistry meterRegistry) {
        return new MicrometerMonitoringService(meterRegistry);
    }
}
