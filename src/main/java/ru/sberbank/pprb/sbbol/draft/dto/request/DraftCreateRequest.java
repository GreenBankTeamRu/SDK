package ru.sberbank.pprb.sbbol.draft.dto.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * Запрос на создание черновика
 */
public class DraftCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -6843077719803712605L;

    /**
     * Наименование черновика
     */
    @NotNull(message = "Параметр name должен быть заполнен")
    @Length(min = 1, max = 20, message = "Длина поля name должна быть от 1 до 20 символов")
    private String name;

    /**
     * Содержимое черновика
     */
    @Length(max = 4000, message = "Длина поля content не должна превышать 4000 символов")
    private String content;

    /**
     * Идентификатор организации в Digital
     */
    @NotNull(message = "Параметр digitalId должен быть заполнен")
    private String digitalId;

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

    public String getDigitalId() {
        return digitalId;
    }

    public void setDigitalId(String digitalId) {
        this.digitalId = digitalId;
    }
}
