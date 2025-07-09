package ru.sberbank.sbbol.sberbusinessapi.model;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Signature {

    /**
     * Значение электронной подписи, закодированное в Base64
     */
    @NotBlank
    private String base63Encoded;

    /**
     * Уникальный идентификатор сертификата ключа проверки электронной подписи (UUID)
     */
    @NotBlank
    private String certificateUuid;
}