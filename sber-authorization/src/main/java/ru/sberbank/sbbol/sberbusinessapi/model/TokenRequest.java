package ru.sberbank.sbbol.sberbusinessapi.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class TokenRequest extends SbbapiBaseRequest {
    /**
     * Если первый вход - Указывает тип предоставления, который используется для запроса токена доступа
     * Значение должно быть authorization_code
     *
     * Если обновление токены - Указывает тип предоставления, который используется для запроса токена доступа
     * Значение должно быть refresh_token
     */
    @NotNull(message = "Grant type is required.")
    @JsonProperty("grant_type")
    @Setter
    private GrantType grantType;

    /**
     * Код авторизации.
     * В параметре необходимо использовать значение code, полученное на адрес redirect_uri
     * при успешной аутентификации пользователя по URL аутентификации /v2/oauth/authorize.
     * Каждый code может быть использован только один раз.
     */
    @Pattern(regexp = "^[a-zA-Z0-9]{38}$", message = "Invalid code format.")
    @Getter
    private String code;

    /**
     * Значение refresh_token полученное при обмене кода авторизации на access_token
     */
    @JsonProperty("refresh_token")
    @Getter
    private String refreshToken;

    /**
     * Уникальный идентификатор вашей Платформы, полученный при подключении к Sber API
     */
    @NotBlank(message = "Client ID is required.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Invalid client ID format.")
    @JsonProperty("client_id")
    private String clientId;

    /**
     * Адрес вашей Платформы, на который СберБизнес ID возвращает пользователя при успешной аутентификации.
     * Должен в точности совпадать со значением, переданным ранее при вызове ресурса /v2/oauth/authorize
     */
    @Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?([a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,})(:[0-9]{1,5})?\\/?.*$", message = "Invalid redirect URI format.")
    @JsonProperty("redirect_uri")
    private String redirectUri;

    /**
     * Пароль вашей Платформы. Вы его впервые получаете при подключении к Sber API и в последующем периодически обновляете.
     */
    @NotBlank(message = "Client secret is required.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Invalid client secret format.")
    @JsonProperty("client_secret")
    private String clientSecret;

    /**
     * Параметр в соответствии с надстройкой PKCE над протоколом OAuth 2.0.
     * Раскодированное значение code_challenge переданное ранее при вызове ресурса /authorize.
     */
    @JsonProperty("code_verifier")
    private String codeVerifier;

    public enum GrantType {
        AUTHORIZATION_CODE,
        REFRESH_TOKEN
    }
}
