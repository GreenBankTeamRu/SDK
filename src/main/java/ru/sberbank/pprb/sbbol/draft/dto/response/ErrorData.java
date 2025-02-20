package ru.sberbank.pprb.sbbol.draft.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

/**
 * Ошибка
 */
public class ErrorData implements Serializable {

    @Serial
    private static final long serialVersionUID = -772744282863394181L;

    /**
     * Код ошибки
     */
    private String errorCode;

    /**
     * Наименование ошибки
     */
    private String errorName;

    /**
     * Описание ошибки
     */
    private String errorDesc;

    @JsonCreator
    public ErrorData(@JsonProperty("errorCode") String errorCode,
                     @JsonProperty("errorName") String errorName,
                     @JsonProperty("errorDesc") String errorDesc) {
        this.errorCode = errorCode;
        this.errorName = errorName;
        this.errorDesc = errorDesc;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    @Override
    public String toString() {
        return "ErrorData{" +
                "errorCode='" + errorCode + '\'' +
                ", errorName='" + errorName + '\'' +
                ", errorDesc='" + errorDesc + '\'' +
                '}';
    }

}
