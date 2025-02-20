--liquibase formatted sql


--changeset smorzhok-da:draft_table
CREATE TABLE DRAFT
(
    ID               UUID               PRIMARY KEY,
    VERSION          SMALLINT default 0 NOT NULL,
    DIGITALID        VARCHAR(38)        NOT NULL,
    NAME             VARCHAR(20)        NOT NULL,
    CONTENT          VARCHAR(4000)      NULL,
    CREATE_DATE      TIMESTAMP(0)       NOT NULL,
    LAST_MODIFY_DATE TIMESTAMP(0)       NOT NULL
);

COMMENT ON TABLE DRAFT IS 'Черновик';
COMMENT ON COLUMN DRAFT.ID IS 'Идентификатор записи';
COMMENT ON COLUMN DRAFT.VERSION IS 'Версия (служебное поле Hibernate)';
COMMENT ON COLUMN DRAFT.DIGITALID IS 'Идентификатор Личного кабинета';
COMMENT ON COLUMN DRAFT.NAME IS 'Наименование черновика';
COMMENT ON COLUMN DRAFT.CONTENT IS 'Содержимое черновика';
COMMENT ON COLUMN DRAFT.CREATE_DATE IS 'Дата создания черновика';
COMMENT ON COLUMN DRAFT.LAST_MODIFY_DATE IS 'Дата последнего изменения черновика';
--rollback DROP TABLE DRAFT


--changeset smorzhok-da:draft_index
CREATE UNIQUE INDEX UX_DRAFT_NAME ON DRAFT (NAME);
--rollback DROP INDEX UX_DRAFT_NAME


-- changeset smorzhok-da:hibernate_se_t_crtj_clientlock
-- Служебная таблицы HibernateSE
create table T_CRTJ_CLIENTLOCK
(
    CLIENT_ID           varchar(255) not null,
    SYS_LASTCHANGEDATE  timestamp    not null,
    MIGRATIONSTATUS     integer      not null,
    SYS_ISDELETED       boolean      not null,
    SYS_PARTITIONID     integer      not null,
    SYS_OWNERID         varchar(255),
    SYS_RECMODELVERSION varchar(255),
    CHGCNT              bigint,
    SILOCK              integer      not null,
    primary key (CLIENT_ID)
);

COMMENT ON TABLE T_CRTJ_CLIENTLOCK IS 'Служебная таблица HibernateSE';
--rollback DROP TABLE T_CRTJ_CLIENTLOCK

-- changeset smorzhok-da:hibernate_se_t_crtj_clientlockevent
-- Служебная таблицы HibernateSE
create table T_CRTJ_CLIENTLOCKEVENT
(
    EVENT_ID            varchar(255) not null,
    SYS_LASTCHANGEDATE  timestamp    not null,
    CLIENT_ID           varchar(255) not null,
    INFO                varchar(255),
    TIMESTAMP_          timestamp,
    SYS_RECMODELVERSION varchar(255),
    GOTDATA             boolean,
    GOTULCK             boolean,
    GOTLCK              boolean,
    primary key (EVENT_ID)
);

COMMENT ON TABLE T_CRTJ_CLIENTLOCKEVENT IS 'Служебная таблица HibernateSE';
--rollback DROP TABLE T_CRTJ_CLIENTLOCKEVENT
