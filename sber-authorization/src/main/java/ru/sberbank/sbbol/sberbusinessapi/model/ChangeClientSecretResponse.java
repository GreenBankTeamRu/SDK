package ru.sberbank.sbbol.sberbusinessapi.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ChangeClientSecretResponse {

    /**
     * Срок действия нового Client Secret (в днях)
     */
    @Getter
    @Setter
    private int clientSecretExpiration;


    /**
     * Новый пароль вашей Платформы
     */
    @Setter
    @Getter
    @Pattern(regexp = "^[a-zA-Z0-9]{8,256}$", message = "Invalid client secret format.")
    @JsonProperty("new_client_secret")
    private String newClientSecret;
}
