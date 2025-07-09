package ru.sberbank.sbbol.sberbusinessapi.interceptors;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class UserAgentInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request newRequest = originalRequest.newBuilder()
                .header("User-Agent", "SberApiSDK/" + getVersionSDK())
                .build();

        return chain.proceed(newRequest);
    }

    private String getVersionSDK() {
        return getClass().getPackage().getImplementationVersion();
    }
}
