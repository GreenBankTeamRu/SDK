package ru.sberbank.pprb.sbbol.draft.dto.request;

import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * Запрос на изменение черновика
 */
public class DraftUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -2585045176446182956L;

    /**
     * Содержимое черновика
     */
    @Length(max = 4000, message = "Длина поля content не должна превышать 4000 символов")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
