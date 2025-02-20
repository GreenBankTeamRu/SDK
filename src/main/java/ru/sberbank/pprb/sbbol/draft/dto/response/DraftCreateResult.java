package ru.sberbank.pprb.sbbol.draft.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * Запрос на создание черновика
 */
public class DraftCreateResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 364264728284569740L;

    /**
     * Идентификатор созданного черновика
     */
    private final UUID guid;

    @JsonCreator
    public DraftCreateResult(UUID guid) {
        this.guid = guid;
    }

    public UUID getGuid() {
        return guid;
    }

}
