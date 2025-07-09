package ru.sberbank.sbbol.sberbusinessapi.api;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import ru.sberbank.sbbol.sberbusinessapi.factory.CustomConverterFactory;
import ru.sberbank.sbbol.sberbusinessapi.interceptors.MaskingLoggingInterceptor;
import ru.sberbank.sbbol.sberbusinessapi.interceptors.RetryInterceptor;
import ru.sberbank.sbbol.sberbusinessapi.interceptors.UserAgentInterceptor;
import ru.sberbank.sbbol.sberbusinessapi.model.clientinfo.ClientInfoResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.AcceptanceAdvance;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.CertRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.CertRequestEIO;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.CryptoInfoEIOResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.CryptoInfoResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.dictionary.DictionaryDtoResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.dictionary.DictionaryResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.payment.FintechPayment;
import ru.sberbank.sbbol.sberbusinessapi.model.payment.FintechPaymentDocState;
import ru.sberbank.sbbol.sberbusinessapi.model.payroll.FintechPayroll;
import ru.sberbank.sbbol.sberbusinessapi.model.payroll.FintechPayrollState;
import ru.sberbank.sbbol.sberbusinessapi.model.statement.FintechStatementSummary;
import ru.sberbank.sbbol.sberbusinessapi.model.statement.FintechStatementTransaction;
import ru.sberbank.sbbol.sberbusinessapi.model.statement.FintechStatementTransactions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class H2hApiImpl extends ApiClient implements H2hApi {
    private static final Logger log = LoggerFactory.getLogger(H2hApiImpl.class);
    private final H2hApiService h2hApiService;


    public H2hApiImpl(HttpClientFactory factory) {
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
        this.h2hApiService = retrofit.create(H2hApiService.class);
    }

    @Override
    public DictionaryResponse getDictionary(@NotBlank String accessToken, String dictType) {
        DictionaryDtoResponse dicts = executeRequest(h2hApiService.getDictionary(accessToken, dictType));
        String unzipDict = decodeAndUnzip(dicts.getArchive());
        return new DictionaryResponse(unzipDict, dicts.getName());
    }

    @Override
    public ClientInfoResponse getClientInfo(@NotBlank String accessToken) {
        return executeRequest(h2hApiService.getClientInfo(accessToken));
    }

    @Override
    public CryptoInfoResponse getCrypto(@NotBlank String accessToken) {
        return executeRequest(h2hApiService.getCrypto(accessToken));
    }

    @Override
    public CryptoInfoEIOResponse getCryptoEio(@NotBlank String accessToken) {
        return executeRequest(h2hApiService.getCryptoEio(accessToken));
    }

    @Override
    public CertRequest certificateRequest(@NotBlank String accessToken, CertRequest request) {
        validate(request);
        return executeRequest(h2hApiService.certificateRequest(accessToken, request));
    }

    @Override
    public CertRequestEIO getCertRequestEio(@NotBlank String accessToken, CertRequestEIO request) {
        validate(request);
        return executeRequest(h2hApiService.certificateRequestEIO(accessToken, request));
    }

    @Override
    public void activateCertEIO(@NotBlank String accessToken, String externalId) {
        executeRequest(h2hApiService.activateCertEIO(accessToken, externalId));
    }

    @Override
    public void activateCert(@NotBlank String accessToken, String externalId) {
        executeRequest(h2hApiService.activateCert(accessToken, externalId));
    }

    @Override
    public byte[] printCertificate(String accessToken, String externalId) {
        return executeBinaryRequest(h2hApiService.printCert(accessToken, externalId));
    }

    @Override
    public AcceptanceAdvance getCertState(@NotBlank String accessToken, String externalId) {
        return executeRequest(h2hApiService.getCertState(accessToken, externalId));
    }

    @Override
    public AcceptanceAdvance getCertEIOState(@NotBlank String accessToken, String externalId) {
        return executeRequest(h2hApiService.getCertStateEIO(accessToken, externalId));
    }

    @Override
    public FintechPayment createPayment(String accessToken, @Valid FintechPayment payment) {
        validate(payment);
        return executeRequest(h2hApiService.createPayment(accessToken, payment));
    }

    @Override
    public FintechPayment getPayment(String accessToken, String externalId) {
        return executeRequest(h2hApiService.getPayment(accessToken, externalId));
    }

    @Override
    public FintechPaymentDocState getPaymentDocState(String accessToken, String externalId) {
        return executeRequest(h2hApiService.getPaymentDocState(accessToken, externalId));
    }

    @Override
    public FintechStatementSummary getStatementSummary(String accessToken, String accountNumber, LocalDate statementDate) {
        return executeRequest(h2hApiService.getStatementSummary(accessToken, accountNumber, statementDate));
    }

    @Override
    public FintechStatementTransaction getStatementTransactionId(String accessToken, String id, String accountNumber, LocalDate operationDate) {
        return executeRequest(h2hApiService.getStatementTransactionId(accessToken, id, accountNumber, operationDate));
    }

    @Override
    public FintechStatementTransactions getStatementTransactions(String accessToken, String accountNumber, LocalDate statementDate, int page, String curFormat) {
        if (curFormat != null && !"curTransfer".equals(curFormat) && !"swiftTransfer".equals(curFormat)) {
            throw new IllegalArgumentException("curTransfer должен соответствовать паттерну: ^(curTransfer|swiftTransfer)$");
        }
        return executeRequest(h2hApiService.getStatementTransactions(accessToken, accountNumber, statementDate, page, curFormat));
    }

    @Override
    public FintechPayroll createPayroll(String accessToken, @Valid FintechPayroll payroll) {
        validate(payroll);
        return executeRequest(h2hApiService.createPayroll(accessToken, payroll));
    }

    @Override
    public FintechPayroll getPayroll(String accessToken, String externalId) {
        return executeRequest(h2hApiService.getPayroll(accessToken, externalId));
    }

    @Override
    public FintechPayrollState getPayrollState(String accessToken, String externalId) {
        return executeRequest(h2hApiService.getPayrollState(accessToken, externalId));
    }

    public static String decodeAndUnzip(String base64EncodedZip) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedZip);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedBytes);

            ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);

            ZipEntry entry;
            StringBuilder content = new StringBuilder();
            while ((entry = zipInputStream.getNextEntry()) != null) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
                content.append(outputStream.toString(StandardCharsets.UTF_8.name()));
                zipInputStream.closeEntry();
            }

            zipInputStream.close();
            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException("Произошла ошибка при декодировании архива: ", e);
        }
    }
}
