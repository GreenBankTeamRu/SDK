package ru.sberbank.sbbol.sberbusinessapi.interceptors;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class MaskingLoggingInterceptor implements Interceptor {

    private final boolean enableLogging;
    private static final Map<String, Function<String, String>> MASKING_RULES = new HashMap();
    private static final Map<String, String> FIELD_MASKING_TYPES = new HashMap();
    private static final List<String> EXCLUDES_LOG_URI = Arrays.asList("/v1/client-info", "/fintech/api/v1/dicts");

    private static final Pattern JSON_FIELD_PATTERN = Pattern.compile(
            "\"([^\"]+)\"\\s*:\\s*(\"[^\"]*\")",
    Pattern.CASE_INSENSITIVE
            );
    private static final Pattern URLENCODED_PARAM_PATTERN = Pattern.compile(
            "([?&])(?<key>[^=]+)=(?<value>[^&]*)",
    Pattern.CASE_INSENSITIVE);

    static {
        MASKING_RULES.put("default", value -> "****");

        MASKING_RULES.put("email", value -> {
            if (value == null || value.isEmpty()) return value;
            if (value.length() <= 4) return "****";
            return "****" + value.substring(4);
        });

        MASKING_RULES.put("account", value -> {
            if (value == null || value.isEmpty()) return value;
            String digits = value.replaceAll("[^0-9]", "");
            if (digits.length() <= 6) return "****";
            return digits.substring(0, 8) + "*******" + digits.substring(digits.length() - 4);
        });

        MASKING_RULES.put("phone", value -> {
            if (value == null || value.isEmpty()) return value;
            String digits = value.replaceAll("[^0-9]", "");
            if (digits.length() <= 4) return "****";
            return digits.charAt(0) + "****" + digits.substring(digits.length() - 2);
        });

        MASKING_RULES.put("number", value -> {
            if (value == null || value.isEmpty()) return value;
            return "****"; // Можно изменить на более сложную логику
        });

        FIELD_MASKING_TYPES.put("Authorization", "default");
        FIELD_MASKING_TYPES.put("authorization", "default");
        FIELD_MASKING_TYPES.put("X-Auth-Token", "default");
        FIELD_MASKING_TYPES.put("x-auth-token", "default");
        FIELD_MASKING_TYPES.put("client_secret", "default");
        FIELD_MASKING_TYPES.put("password", "default");
        FIELD_MASKING_TYPES.put("email", "email");
        FIELD_MASKING_TYPES.put("corrAccountNumber", "account");
        FIELD_MASKING_TYPES.put("accountNumber", "account");
        FIELD_MASKING_TYPES.put("payerAccount", "account");
        FIELD_MASKING_TYPES.put("payeeAccount", "account");
        FIELD_MASKING_TYPES.put("payeeBankCorrAccount", "account");
        FIELD_MASKING_TYPES.put("payerBankCorrAccount", "account");
        FIELD_MASKING_TYPES.put("account", "account");
        FIELD_MASKING_TYPES.put("phone_number", "phone");
        FIELD_MASKING_TYPES.put("serialNumber", "default");
        FIELD_MASKING_TYPES.put("amount", "number");
        FIELD_MASKING_TYPES.put("access_token", "default");
        FIELD_MASKING_TYPES.put("new_client_secret", "default");
        FIELD_MASKING_TYPES.put("refresh_token", "default");
        FIELD_MASKING_TYPES.put("cert", "default");
        FIELD_MASKING_TYPES.put("authPersonName", "fio");
        FIELD_MASKING_TYPES.put("lastName", "fio");
        FIELD_MASKING_TYPES.put("middleName", "fio");
        FIELD_MASKING_TYPES.put("purpose", "default");
        FIELD_MASKING_TYPES.put("paymentPurpose", "default");
        FIELD_MASKING_TYPES.put("archive", "default");
        FIELD_MASKING_TYPES.put("payerName", "default");
        FIELD_MASKING_TYPES.put("payeeName", "default");
        FIELD_MASKING_TYPES.put("inn", "default");
        FIELD_MASKING_TYPES.put("payerInn", "default");
        FIELD_MASKING_TYPES.put("payeeInn", "default");
        FIELD_MASKING_TYPES.put("INN", "default");
        FIELD_MASKING_TYPES.put("orgTaxNumber", "default");
        FIELD_MASKING_TYPES.put("authPersonTelfax", "default");
    }

    public MaskingLoggingInterceptor(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

    public MaskingLoggingInterceptor() {
        this(false);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        boolean isExclude = EXCLUDES_LOG_URI.stream().anyMatch(path -> request.url().encodedPath().contains(path));

        if (enableLogging) {
            logRequest(request);
        }

        Response response = chain.proceed(request);

        if (enableLogging && !isExclude) {
            logResponse(response);
        }

        return response;
    }

    private void logRequest(Request request) throws IOException {
        log.info("Request: {} {}", request.method(), maskUrl(request.url().toString()));

        for (int i = 0; i < request.headers().size(); i++) {
            String name = request.headers().name(i);
            String value = request.headers().value(i);

            String maskedValue = FIELD_MASKING_TYPES.containsKey(name)
                    ? maskValue(name, value)
                    : value;
            log.debug("Header: {}: {}", name, maskedValue);
        }

        if (request.body() != null) {
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            String body = buffer.readUtf8();
            log.debug("Request Body: {}", maskBodyContent(body));


            Request newRequest = request.newBuilder()
                    .method(request.method(), okhttp3.RequestBody.create(
                            buffer.readByteString(),
                            request.body().contentType()))
                    .build();
            request = newRequest;
        }
    }

    private void logResponse(Response response) throws IOException {
        log.debug("Response Code: {}", response.code());

        for (int i = 0; i < response.headers().size(); i++) {
            String name = response.headers().name(i);
            String value = response.headers().value(i);

            String maskedValue = FIELD_MASKING_TYPES.containsKey(name)
                    ? maskValue(name, value)
                    : value;
            log.debug("Header: {}: {}", name, maskedValue);
        }

        if (response.body() != null) {
            String body = response.peekBody(1024 * 1024).string();
            log.debug("Response Body: {}", maskBodyContent(body));
        }
    }

    private String maskUrl(String url) {
        if (url == null) return null;

        Matcher matcher = URLENCODED_PARAM_PATTERN.matcher(url);
        StringBuffer result = new StringBuffer();

        try {
            while (matcher.find()) {
                String key = matcher.group("key");
                String value = matcher.group("value");


                String decodedValue = URLDecoder.decode(value);


                String maskedValue = maskValue(key, decodedValue);


                String encodedValue = URLEncoder.encode(maskedValue, StandardCharsets.UTF_8.toString());


                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(1) + key + "=" + encodedValue));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error decoding URL", e);
        }

        matcher.appendTail(result);

        return result.toString();
    }

    private String maskBodyContent(String body) {
        if (body == null || body.isEmpty()) return body;

        if (body.trim().startsWith("{")) {

            return maskJsonBodyContent(body);
        } else if (body.contains("=") && body.contains("&")) {

            Matcher matcher = URLENCODED_PARAM_PATTERN.matcher(body);
            StringBuffer result = new StringBuffer();

            while (matcher.find()) {
                String key = matcher.group("key");
                String value = matcher.group("value");


                String maskedValue = maskValue(key, value);


                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(1) + key + "=" + maskedValue));
            }

            matcher.appendTail(result);

            return result.toString();
        }

        return body;
    }

    private String maskJsonBodyContent(String body) {
        try {
            StringBuilder maskedBody = new StringBuilder();
            int start = 0;

            Matcher matcher = JSON_FIELD_PATTERN.matcher(body);
            while (matcher.find()) {
                maskedBody.append(body, start, matcher.start());

                String key = matcher.group(1);
                String value = matcher.group(2);

                String maskedValue = maskValue(key, value);

                maskedBody.append("\"").append(key).append("\":\"").append(maskedValue).append("\"");

                start = matcher.end();
            }

            maskedBody.append(body.substring(start));
            return maskedBody.toString();
        } catch (Exception e) {
            log.error("Error masking JSON body: {}", e.getMessage());
            return null;
        }
    }

    private String maskValue(String key, String value) {
        if (value == null || value.isEmpty()) return value;

        String maskingType = FIELD_MASKING_TYPES.get(key);
        if (maskingType == null) return value;
        Function<String, String> maskingFunction = MASKING_RULES.getOrDefault(maskingType, MASKING_RULES.get("default"));
        return maskingFunction.apply(value);
    }
}