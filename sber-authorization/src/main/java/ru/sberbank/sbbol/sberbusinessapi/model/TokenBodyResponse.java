package ru.sberbank.sbbol.sberbusinessapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenBodyResponse {

    /**
     * Авторизационный токен доступа
     */
    @NotBlank
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * Тип токена. Всегда возвращается значение Bearer
     */
    @NotBlank
    @JsonProperty("token_type")
    private String tokenType;

    /**
     * Токен обновления. Используется для обновления Access Token.
     * Подробнее про применение значения refresh_token см. в Обновление токена доступа (access_token)
     * Срок жизни refresh_token составляет 180 дней.
     */
    @NotBlank
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * Срок жизни токена в секундах.
     * Срок жизни access_token составляет 60 минут.
     */
    @NotBlank
    @JsonProperty("expires_in")
    private String expiresIn;

    /**
     * Набор атрибутов (claim) и операций, которые будут доступны Платформе после авторизации клиента.
     */
    @NotBlank
    @JsonProperty("scope")
    private String scope;

    /**
     * Закодированный в Base64URL набор атрибутов клиента, необходимых для идентификации пользователя.
     * Атрибуты разделены символами «.», каждый необходимо декодировать отдельно. Подробнее о декодировании и параметрах id_token см. в ID Token
     */
    @NotBlank
    @JsonProperty("id_token")
    private String idToken;
}
