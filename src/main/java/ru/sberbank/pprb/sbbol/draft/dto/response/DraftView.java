package ru.sberbank.pprb.sbbol.draft.dto.response;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * Черновик
 */
public class DraftView implements Serializable {

    @Serial
    private static final long serialVersionUID = -4483779289375034809L;

    /**
     * Уникальный идентификатор черновика
     */
    private UUID guid;

    /**
     * Идентификатор организации в Digital
     */
    private String digitalId;

    /**
     * Наименование черновика
     */
    private String name;

    /**
     * Содержимое черновика
     */
    private String content;

    /**
     * Дата создания черновика
     */
    private Instant createDate;

    /**
     * Дата изменения черновика
     */
    private Instant modifyDate;

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getDigitalId() {
        return digitalId;
    }

    public void setDigitalId(String digitalId) {
        this.digitalId = digitalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
    }
}
