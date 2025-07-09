package ru.sberbank.sbbol.sberbusinessapi.api;

import javax.validation.Valid;
import ru.sberbank.sbbol.sberbusinessapi.model.ChangeClientSecretRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.ChangeClientSecretResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.RevokeTokenRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.TokenBodyResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.TokenRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.TokenResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.UserInfoResponse;

public interface AuthorizationApiClient {

    /**
     * Метод получения токена
     * @param request запрос на получение токена
     * @param needUserInfo  признак необходимости дешифровывать IdToken
     * @return ответ
     */
    TokenResponse getAccessToken(@Valid TokenRequest request, boolean needUserInfo);

    /**
     * Метод обновления токена
     * @param request запрос на обновления токена
     * @return ответ
     */
    TokenBodyResponse getRefreshToken(@Valid TokenRequest request);

    /**
     * Метод обновления секрета клиента
     * @param request запрос на обновления секрета клиента
     * @return Срок действия нового Client Secret
     */
    ChangeClientSecretResponse getRefreshClientSecret(@Valid ChangeClientSecretRequest request);

    /**
     * Метод отзыва токена
     * @param request запрос на отзыва токена
     */
    void getRevokeToken(@Valid RevokeTokenRequest request);

    /**
     * Метод получения информации о пользователе
     * @param accessToken токен
     * @return ответ
     */
    UserInfoResponse getUserInfo(String accessToken);
}
