package ru.sberbank.sbbol.sberbusinessapi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import ru.sberbank.sbbol.sberbusinessapi.factory.ValidatorFactoryProvider;
import ru.sberbank.sbbol.sberbusinessapi.model.ErrorResponse;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ApiClient {
    private static final String X_REQUEST_ID = "X-Request-Id";
    private static final String X_RQ_UID = "X-Rq-Uid";

    public static <RS> RS executeRequest(Call<RS> call) {
        long startTime = System.currentTimeMillis();

        try {
            retrofit2.Response<RS> response = call.execute();
            long elapsedTime = System.currentTimeMillis() - startTime;
            String requestId = response.headers().get(X_REQUEST_ID) != null ? response.headers().get(X_REQUEST_ID) : response.headers().get(X_RQ_UID);
            if (response.isSuccessful()) {
                log.info("Запрос выполнен успешно. Request ID: " + requestId + ", Time: " + elapsedTime + " мс");
                return response.body();
            } else {
                log.error("Ошибка запроса. Request ID: " + requestId + ", Time: " + elapsedTime + " мс");
                throw new RuntimeException(getErrorResponse(response));
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при отправке запроса в SbbApi:", e);
        }
    }

    public static byte[] executeBinaryRequest(Call<byte[]> call) {
        try {
            retrofit2.Response<byte[]> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                throw new RuntimeException(getErrorResponse(response));
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при отправке запроса в SbbApi:", e);
        }
    }

    public static String checkHost(String host) {
        return !host.endsWith("/") ? host + "/" : host;
    }

    public String decodeBase64Url(String base64UrlEncodedString) {
        return new String(Base64.getDecoder()
                .decode(base64UrlEncodedString
                        .replace('-', '+')
                        .replace('_', '/')));
    }

    public static String getErrorResponse(retrofit2.Response<?> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponse errorResponse = objectMapper.readValue(response.errorBody().string(), ErrorResponse.class);
            return "Ошибка: " + errorResponse.getError() + ", Описание: "
                    + errorResponse.getErrorDescription() + ", RequestId: " + response.headers().get(X_REQUEST_ID);
        } catch (IOException e) {
            return "Ошибка при чтении ответа сервера.";
        }
    }

    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = ValidatorFactoryProvider.getValidator().validate(object);
        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining("; "));
            throw new IllegalArgumentException("Validation errors: " + errorMessages);
        }
    }
}
