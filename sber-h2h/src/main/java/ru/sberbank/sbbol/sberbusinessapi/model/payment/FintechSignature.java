package ru.sberbank.sbbol.sberbusinessapi.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FintechSignature {
    @JsonProperty("base64Encoded")
    @NotNull(message = "Значение электронной подписи обязательно")
    private String base64Encoded;

    @JsonProperty("certificateUuid")
    @NotNull(message = "UUID сертификата ключа проверки ЭП обязателен")
    private UUID certificateUuid;
}
