package ru.sberbank.sbbol.sberbusinessapi.interceptors;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class RetryInterceptor  implements Interceptor {
    private static final int MAX_RETRIES = 3;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        IOException exception = null;
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                response = chain.proceed(chain.request());
                if (response.code() != 429 && response.code() != 500) {
                    return response;
                }
                // Если это не последняя попытка, закрываем текущий ответ и ждем 1 секунду перед следующей попыткой
                if (i != MAX_RETRIES - 1) {
                    response.close();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Попытка повторного соединения была прервана", e);
                    }
                }
            } catch (IOException e) {
                exception = e;
            }
        }
        if (exception != null) {
            throw exception;
        }
        return response;
    }
}
