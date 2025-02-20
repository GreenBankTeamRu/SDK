-- liquibase formatted sql

-- changeset kiporov-ds:hibernate_se_t_crtj_clientlock
ALTER TABLE T_CRTJ_CLIENTLOCK ADD COLUMN TXID VARCHAR(36);
--rollback ALTER TABLE T_CRTJ_CLIENTLOCK DROP COLUMN TXID;

--changeset belosludtsev-em:t_crtj_clientlockevent_pkey_drop dbms:postgresql
ALTER TABLE T_CRTJ_CLIENTLOCKEVENT DROP CONSTRAINT t_crtj_clientlockevent_pkey;
--rollback ALTER TABLE T_CRTJ_CLIENTLOCKEVENT ADD PRIMARY KEY (event_id);

--changeset belosludtsev-em:T_CRTJ_CLIENTLOCKEVENT_missed_pk dbms:postgresql
ALTER TABLE T_CRTJ_CLIENTLOCKEVENT  ADD CONSTRAINT T_CRTJ_CLIENTLOCKEVENT_PKEY PRIMARY KEY (event_id, client_id);
--rollback ALTER TABLE T_CRTJ_CLIENTLOCKEVENT DROP CONSTRAINT T_CRTJ_CLIENTLOCKEVENT_PKEY;

-- Служебная таблицы HibernateSE
--changeset belosludtsev-em:T_CRTJ_STANDIN_SERVICE_add
create table T_CRTJ_STANDIN_SERVICE
(
    PARTITION_ID       varchar(255)                                          not null,
    SYS_LASTCHANGEDATE timestamp without time zone default CURRENT_TIMESTAMP not null,
    PREV_STATE         varchar(36),
    CUR_STATE          varchar(36),
    CONF_STATE         varchar(36),
    LOCK_VER           bigint                                                not null,
    CUR_VER            bigint                                                not null,
    CONF_VER           bigint                                                not null,
    LAST_HKEY          varchar(255),
    ERROR_TX           varchar(36),
    CONSTRAINT T_CRTJ_STANDIN_SERVICE_PK PRIMARY KEY (PARTITION_ID)
    );

COMMENT ON TABLE T_CRTJ_STANDIN_SERVICE IS 'Служебная таблица HibernateSE';
--rollback DROP TABLE T_CRTJ_STANDIN_SERVICE

-- changeset kiporov-ds:hibernate_se_t_crtj_confirmations
-- Служебная таблицы HibernateSE
create table T_CRTJ_CONFIRMATIONS
(
    TX_ID       varchar(36) not null,
    SYS_LASTCHANGEDATE timestamp without time zone default CURRENT_TIMESTAMP not null,
    CONSTRAINT T_CRTJ_CONFIRMATIONS_PK PRIMARY KEY (TX_ID)
    );

COMMENT ON TABLE T_CRTJ_CONFIRMATIONS IS 'Служебная таблица HibernateSE';
--rollback DROP TABLE T_CRTJ_CONFIRMATIONS