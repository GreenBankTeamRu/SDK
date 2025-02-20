-- liquibase formatted sql

--changeset sber_business_api_sdk:1682764142107_tsa
create table INIT_DATA_PARTITIONS
(
    -- Уникальный идентификатор сессии инита
    LOADING_ID       VARCHAR(100),
    -- Названия типа по которому происходит инит
    TABLE_NAME       VARCHAR(200),
    -- Номер пачки (для стратегии Hashcode - количество пачек)
    PARTITION_NUMBER INTEGER,
    -- используется для стратегий SIMPLE_ID, SIMPLE_OR_COMPOSITE_ID_AUTOSELECT, CompositeIdBigTable
    -- этим стратегиям нужно запоминать диапазоны ид пачек. Поле START_ID - это первичный ключ объекта находящегося с краю пачки.
    -- т.е. диапазон определяется двумя строками из INIT_DATA_PARTITIONS. где PartitionNumber+1 - нижнее значение, PartitionNumber+0 - верхнее значение
    START_ID         VARCHAR(200),
    -- Используется для стратегий COMPOSITE_ID, SIMPLE_OR_COMPOSITE_ID_AUTOSELECT.
    -- содержит перечисление первичных ключей входящих в пачку. формат component1_component2_..._componentN;component1_component2_..._componentN;...
    IDS              TEXT,
    -- Статус инита - PENDING, READY, ERROR
    STATUS           VARCHAR(10),
    PRIMARY KEY (LOADING_ID, PARTITION_NUMBER)
);
COMMENT ON TABLE INIT_DATA_PARTITIONS IS 'Системная таблица для выгрузки информации в фабрику данных';
create index LOADING_ID_INDEX
    on INIT_DATA_PARTITIONS (LOADING_ID);
create index ENTITY_NAME_INDEX
    on INIT_DATA_PARTITIONS (TABLE_NAME);

--rollback DROP TABLE IF EXISTS INIT_DATA_PARTITIONS
