package ru.sberbank.sbbol.sberbusinessapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class RevokeTokenRequest extends SbbapiBaseRequest {

    /**
     * Access token пользователя, полученный через SSO.
     */
    @NotBlank(message = "accessToken is required.")
    @JsonProperty("access_token")
    @Setter
    private String accessToken;

    /**
     * Уникальный идентификатор вашей Платформы, полученный при подключении к Sber API
     */
    @NotBlank(message = "Client ID is required.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Invalid client ID format.")
    @JsonProperty("client_id")
    private String clientId;

    /**
     * Пароль вашей Платформы. Вы его впервые получаете при подключении к Sber API и в последующем периодически обновляете.
     */
    @NotBlank(message = "Client secret is required.")
    @JsonProperty("client_secret")
    private String clientSecret;

    /**
     * Значение токена доступа или обновления, который требуется отозвать
     */
    @NotBlank(message = "Token is required.")
    @Pattern(regexp = "^([a-zA-Z0-9]){38}$", message = "Invalid token format.")
    @JsonProperty("token")
    private String token;

    /**
     * Значение токена доступа или обновления, который требуется отозвать
     */
    @JsonProperty("token_type_hint")
    private TokenTypeHint tokenTypeHint;

    public enum TokenTypeHint {
        access_token,
        refresh_token
    }
}
