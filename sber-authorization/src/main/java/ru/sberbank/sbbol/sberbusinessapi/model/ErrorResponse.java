package ru.sberbank.sbbol.sberbusinessapi.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import ru.sberbank.sbbol.sberbusinessapi.serializer.ErrorResponseDeserializer;

@Getter
@JsonDeserialize(using = ErrorResponseDeserializer.class)
public class ErrorResponse {
    private String error;
    private String errorDescription;

    public ErrorResponse(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

}