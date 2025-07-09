package ru.sberbank.sbbol.sberbusinessapi.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.sberbank.sbbol.sberbusinessapi.factory.CustomConverterFactory;
import ru.sberbank.sbbol.sberbusinessapi.interceptors.MaskingLoggingInterceptor;
import ru.sberbank.sbbol.sberbusinessapi.interceptors.RetryInterceptor;
import ru.sberbank.sbbol.sberbusinessapi.interceptors.UserAgentInterceptor;
import ru.sberbank.sbbol.sberbusinessapi.model.ChangeClientSecretRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.ChangeClientSecretResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.RevokeTokenRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.TokenBodyResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.TokenRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.TokenResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.UserInfoResponse;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static ru.sberbank.sbbol.sberbusinessapi.services.ClientSecretGenerator.generateClientSecret;

@Getter
@Setter
@Slf4j
public class AuthorizationApiClientImpl extends ApiClient implements AuthorizationApiClient {
    private final ApiService apiService;
    private final ObjectMapper om = new ObjectMapper();

    private static final String X_REQUEST_ID = "X-Request-Id";
    private static final String X_RQ_UID = "X-Rq-Uid";

    public AuthorizationApiClientImpl(HttpClientFactory factory) {
        OkHttpClient httpClient = factory.createHttpClient().newBuilder()
                .addInterceptor(new MaskingLoggingInterceptor(factory.getIsEnableLogs()))
                .addInterceptor(new RetryInterceptor())
                .addInterceptor(new UserAgentInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(checkHost(factory.getHost()))
                .client(httpClient)
                .addConverterFactory(new CustomConverterFactory())
                .build();
        this.apiService = retrofit.create(ApiService.class);
    }

    public TokenResponse getAccessToken(@Valid TokenRequest request, boolean needUserInfo) {
        validateTokenRequest(request, TokenRequest.GrantType.AUTHORIZATION_CODE, "Не указан код авторизации");
        validate(request);

        TokenBodyResponse tokenBodyResponse = executeRequest(apiService.accessToken(
                request.getGrantType().name().toLowerCase(Locale.ROOT),
                request.getCode(),
                request.getClientId(),
                request.getRedirectUri(),
                request.getClientSecret(),
                request.getCodeVerifier()));

        return buildTokenResponse(tokenBodyResponse, tokenBodyResponse.getIdToken(), needUserInfo);
    }


    public TokenBodyResponse getRefreshToken(@Valid TokenRequest request) {
        validateTokenRequest(request, TokenRequest.GrantType.REFRESH_TOKEN, "Не указан refreshToken");
        validate(request);

        return executeRequest(apiService.refreshToken(
                request.getGrantType().name().toLowerCase(Locale.ROOT),
                request.getRefreshToken(),
                request.getClientId(),
                request.getRedirectUri(),
                request.getClientSecret(),
                request.getCodeVerifier()));
    }

    public ChangeClientSecretResponse getRefreshClientSecret(@Valid ChangeClientSecretRequest request) {
        validate(request);
        String newClientSecret = generateClientSecret();
        ChangeClientSecretResponse changeClientSecretResponse = executeRequest(apiService.changeClientSecret(
                request.getAccessToken(), request.getClientId(),
                request.getClientSecret(), newClientSecret));
        changeClientSecretResponse.setNewClientSecret(newClientSecret);
        return changeClientSecretResponse;
    }

    public void getRevokeToken(@Valid RevokeTokenRequest request) {
        validate(request);
        executeRequest(apiService.revokeToken(request.getAccessToken(),
                request.getClientId(), request.getClientSecret(),
                request.getToken(), request.getTokenTypeHint().name().toLowerCase(Locale.ROOT)));
    }

    public UserInfoResponse getUserInfo(String accessToken) {
        String encodeResponse = executeRequest(apiService.userInfo("Bearer " + accessToken));
        String payload = splitJwt(encodeResponse)[1];
        String decodeBase64Url = decodeBase64Url(payload);
        try {
            return UserInfoResponse.builder()
                    .userInfoBodyResponse(om.readValue(decodeBase64Url, Map.class))
                    .jwt(encodeResponse)
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <RS> RS executeRequest(Call<RS> call) {
        long startTime = System.currentTimeMillis();

        try {
            retrofit2.Response<RS> response = call.execute();
            long elapsedTime = System.currentTimeMillis() - startTime;

            if (response.isSuccessful()) {
                log.info("Запрос выполнен успешно. Request ID: " + extractRequestId(response) + ", Time: " + elapsedTime + " мс");
                return response.body();
            } else {
                log.error("Ошибка запроса. Request ID: " + extractRequestId(response) + ", Time: " + elapsedTime + " мс");
                throw new RuntimeException(getErrorResponse(response));
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при отправке запроса в SbbApi:", e);
        }
    }

    private static <RS> String extractRequestId(Response<RS> response) {
        String requestId = response.headers().get(X_REQUEST_ID);
        if (requestId == null) {
            requestId = response.headers().get(X_RQ_UID);
        }
        return requestId;
    }

    private void validateTokenRequest(TokenRequest request, TokenRequest.GrantType grantType, String errorMessage) {
        request.setGrantType(grantType);
        if (grantType == TokenRequest.GrantType.AUTHORIZATION_CODE && request.getCode() == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private String[] splitJwt(String jwt) {
        String[] blocks = jwt.split("\\.");
        if (blocks.length != 3) {
            throw new IllegalArgumentException("Invalid format. Expected three parts separated by dots.");
        }
        return blocks;
    }

    private TokenResponse buildTokenResponse(TokenBodyResponse tokenBodyResponse, String jwt, boolean needUserInfo) {
        String decodedHeader = null;
        String decodedBody = null;
        String signature = null;

        if (needUserInfo) {
            String[] blocks = splitJwt(jwt);
            decodedHeader = decodeBase64Url(blocks[0]);
            decodedBody = decodeBase64Url(blocks[1]);
            signature = decodeBase64Url(blocks[2]);
        }

        return TokenResponse.builder()
                .tokenBodyResponse(tokenBodyResponse)
                .header(decodedHeader)
                .body(decodedBody)
                .signature(signature)
                .jwt(jwt)
                .build();
    }

    private String maskSensitiveData(String message) {
        // Регулярные выражения для поиска чувствительных данных
        String[] patterns = {
                "\"(password|pwd|creditCard|cvv|token|apiKey|secret)\"\\s*:\\s*\"([^\"]*)\"",
                "\"(cardNumber|accountNumber)\"\\s*:\\s*\"(\\d{4})\\d*(\\d{4})\""
        };

        String result = message;

        // Маскируем простые поля (заменяем все значение звездочками)
        result = result.replaceAll("\"(client_id|pwd)\"\\s*:\\s*\"([^\"]*)\"",
                "\"$1\":\"******\"");

//        // Маскируем номера карт (оставляем только первые 4 и последние 4 цифры)
//        result = result.replaceAll("\"(cardNumber|accountNumber)\"\\s*:\\s*\"(\\d{4})\\d*(\\d{4})\"",
//                "\"$1\":\"$2****$3\"");

        return result;
    }
}