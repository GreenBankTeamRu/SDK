package ru.sberbank.pprb.sbbol.draft.entity;

import com.sbt.pprb.integration.replication.HashKeyProvider;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Модель для создания или изменения записи "Счетчик документов"
 */
@Entity
@DynamicUpdate
public class Draft implements HashKeyProvider {

    /**
     * Идентификатор записи
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Версия объекта
     */
    @Version
    private Short version;

    /**
     * Идентификатор организации в Digital
     */
    @Column(name = "digitalid", nullable = false)
    private String digitalId;

    /**
     * Наименование черновика
     */
    @Column(length = 20, nullable = false)
    private String name;

    /**
     * Содержимое черновика
     */
    @Column(length = 4000, nullable = false)
    private String content;

    /**
     * Дата создания черновика
     */
    @Column(name = "CREATE_DATE")
    @CreationTimestamp
    private Instant createDate;

    /**
     * Дата изменения черновика
     */
    @Column(name = "LAST_MODIFY_DATE")
    @UpdateTimestamp
    private Instant modifyDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Short getVersion() {
        return version;
    }

    public void setVersion(Short version) {
        this.version = version;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Draft that = (Draft) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Transient
    @Override
    public String getHashKey() {
        // ключ хэширования должен быть равномерно распределен и един для всех сущностей в одной транзакции
        return String.valueOf(Objects.hash(id) % 10000);
    }

}
