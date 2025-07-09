package ru.sberbank.sbbol.sberbusinessapi.api;

import javax.validation.constraints.NotBlank;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import ru.sberbank.sbbol.sberbusinessapi.factory.CustomConverterFactory;
import ru.sberbank.sbbol.sberbusinessapi.interceptors.MaskingLoggingInterceptor;
import ru.sberbank.sbbol.sberbusinessapi.interceptors.RetryInterceptor;
import ru.sberbank.sbbol.sberbusinessapi.interceptors.UserAgentInterceptor;
import ru.sberbank.sbbol.sberbusinessapi.model.CryptoprofileType;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentInvoiceBudgetRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentInvoiceFromAnyRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentInvoiceRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentInvoiceResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentState;

public class InstantPaymentApiImpl extends ApiClient implements InstantPaymentApi {
    private final InstantPaymentApiService apiService;
    private static final String TEST_HOST = "https://efs-sbbol-ift-web.testsbi.sberbank.ru:9443";
    private static final String PROM_SMS_HOST = "https://sbi.sberbank.ru:9443";
    private static final String PROM_TOKEN_HOST = "http://localhost:28016";
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_JOSE = "application/jose";

    public InstantPaymentApiImpl(HttpClientFactory factory) {
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
        this.apiService = retrofit.create(InstantPaymentApiService.class);
    }

    public PaymentInvoiceResponse createPaymentInvoice(@NotBlank String accessToken, PaymentInvoiceRequest request) {
        validate(request);
        return executeRequest(apiService.getPaymentInvoice(accessToken, request));
    }

    @Override
    public PaymentInvoiceResponse createPaymentInvoiceBudget(@NotBlank String accessToken, PaymentInvoiceBudgetRequest request) {
        validate(request);
        return executeRequest(apiService.getPaymentInvoiceBudget(accessToken, request));
    }

    @Override
    public PaymentInvoiceResponse createPaymentInvoiceAny(String accessToken, PaymentInvoiceFromAnyRequest request) {
        validate(request);
        return executeRequest(apiService.getPaymentInvoiceAny(accessToken, request));
    }

    @Override
    public String buildPaymentUrl(String externalId, String backUrl, CryptoprofileType cryptoprofileType, String host, Boolean isProd) {
        validate(cryptoprofileType);
        String konturBankUrl = null;
        if (host == null || host.isEmpty()) {
            konturBankUrl = isProd ?
                    cryptoprofileType.equals(CryptoprofileType.SMS) ? PROM_SMS_HOST : PROM_TOKEN_HOST //prod
                    : TEST_HOST; //dev
        } else {
            konturBankUrl = host;
        }

        return String.format("%s/ic/ufs/rpp-light/index.html#/payment-creator/%s?backUrl=%s",
                konturBankUrl, externalId, backUrl);
    }

    @Override
    public PaymentState getPaymentState(String accessToken, String externalId, String accept) {
        if (accept == null || accept.isEmpty()) {
            accept = APPLICATION_JSON;
        } else if (!APPLICATION_JOSE.equals(accept) && !APPLICATION_JSON.equals(accept)) {
            throw new IllegalArgumentException(String.format("Тип %s не поддерживается", accept));
        }

        return executeRequest(apiService.getPaymentState(accessToken, externalId, accept));
    }

    @Override
    public PaymentInvoiceResponse getPayment(String accessToken, String externalId, String accept) {
        if (accept == null || accept.isEmpty()) {
            accept = APPLICATION_JSON;
        } else if (!APPLICATION_JOSE.equals(accept) && !APPLICATION_JSON.equals(accept)) {
            throw new IllegalArgumentException(String.format("Тип %s не поддерживается", accept));
        }

        return executeRequest(apiService.getPayment(accessToken, externalId, accept));
    }
}
