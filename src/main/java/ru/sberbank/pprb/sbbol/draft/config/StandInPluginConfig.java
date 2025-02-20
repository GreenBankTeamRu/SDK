package ru.sberbank.pprb.sbbol.draft.config;

import com.sbt.pprb.integration.changevector.serialization.SerializerType;
import com.sbt.pprb.integration.replication.OrderingControlStrategy;
import com.sbt.pprb.integration.replication.PartitionLockMode;
import com.sbt.pprb.integration.replication.PartitionMultiplyingMode;
import com.sbt.pprb.integration.replication.ReplicationStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "standin.plugin.configuration")
public class StandInPluginConfig {

    private ReplicationStrategy replicationStrategy;

    private SerializerType serializerType;

    private PartitionLockMode partitionLockMode;

    private PartitionMultiplyingMode partitionMultiplyingMode;

    private OrderingControlStrategy orderingControlStrategy;

    public ReplicationStrategy getReplicationStrategy() {
        return replicationStrategy;
    }

    public void setReplicationStrategy(ReplicationStrategy replicationStrategy) {
        this.replicationStrategy = replicationStrategy;
    }

    public SerializerType getSerializerType() {
        return serializerType;
    }

    public void setSerializerType(SerializerType serializerType) {
        this.serializerType = serializerType;
    }

    public PartitionLockMode getPartitionLockMode() {
        return partitionLockMode;
    }

    public void setPartitionLockMode(PartitionLockMode partitionLockMode) {
        this.partitionLockMode = partitionLockMode;
    }

    public PartitionMultiplyingMode getPartitionMultiplyingMode() {
        return partitionMultiplyingMode;
    }

    public void setPartitionMultiplyingMode(PartitionMultiplyingMode partitionMultiplyingMode) {
        this.partitionMultiplyingMode = partitionMultiplyingMode;
    }

    public OrderingControlStrategy getOrderingControlStrategy() {
        return orderingControlStrategy;
    }

    public void setOrderingControlStrategy(OrderingControlStrategy orderingControlStrategy) {
        this.orderingControlStrategy = orderingControlStrategy;
    }
}
