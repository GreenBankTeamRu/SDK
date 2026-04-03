package ru.sberbank.sbbol.sberbusinessapi.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.sberbank.sbbol.sberbusinessapi.model.clientinfo.ClientInfoResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.AcceptanceAdvance;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.CertRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.CertRequestEIO;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.CryptoInfoEIOResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.CryptoInfoResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.dictionary.DictionaryDtoResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.payment.FintechPayment;
import ru.sberbank.sbbol.sberbusinessapi.model.payment.FintechPaymentDocState;
import ru.sberbank.sbbol.sberbusinessapi.model.paymentlink.SbpB2BLinkCreateRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.paymentlink.SbpB2BLinkCreateResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.paymentlink.SbpB2BgetTransactionListResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.payroll.FintechPayroll;
import ru.sberbank.sbbol.sberbusinessapi.model.payroll.FintechPayrollState;
import ru.sberbank.sbbol.sberbusinessapi.model.statement.FintechStatementSummary;
import ru.sberbank.sbbol.sberbusinessapi.model.statement.FintechStatementTransaction;
import ru.sberbank.sbbol.sberbusinessapi.model.statement.FintechStatementTransactions;

import java.time.LocalDate;

public interface H2hApiService {

    @GET("/fintech/api/v1/dicts")
    Call<DictionaryDtoResponse> getDictionary(@Header("Authorization") String accessToken,
                                              @Query("name") String dictType);

    @GET("/fintech/api/v1/client-info")
    Call<ClientInfoResponse> getClientInfo(@Header("Authorization") String accessToken);

    @GET("/fintech/api/v1/crypto")
    Call<CryptoInfoResponse> getCrypto(@Header("Authorization") String accessToken);

    @GET("/fintech/api/v1/crypto/eio")
    Call<CryptoInfoEIOResponse> getCryptoEio(@Header("Authorization") String accessToken);

    @POST("/fintech/api/v2/crypto/cert-requests")
    Call<CertRequest> certificateRequest(@Header("Authorization") String accessToken, @Body CertRequest body);

    @POST("/fintech/api/v2/crypto/cert-requests/eio")
    Call<CertRequestEIO> certificateRequestEIO(@Header("Authorization") String accessToken, @Body CertRequestEIO body);

    @POST("/fintech/api/v1/crypto/cert-requests/eio/{externalId}/activate")
    Call<Void> activateCertEIO(@Header("Authorization") String accessToken, @Path("externalId") String externalId);

    @POST("/fintech/api/v1/crypto/cert-requests/{externalId}/activate")
    Call<Void> activateCert(@Header("Authorization") String accessToken, @Path("externalId") String externalId);

    @GET("/fintech/api/v2/crypto/cert-requests/{externalId}/print")
    Call<byte[]> printCert(@Header("Authorization") String accessToken, @Path("externalId") String externalId);

    @GET("/fintech/api/v1/crypto/cert-requests/{externalId}/state")
    Call<AcceptanceAdvance> getCertState(@Header("Authorization") String accessToken, @Path("externalId") String externalId);

    @GET("/fintech/api/v1/crypto/cert-requests/eio/{externalId}/state")
    Call<AcceptanceAdvance> getCertStateEIO(@Header("Authorization") String accessToken, @Path("externalId") String externalId);

    @POST("/fintech/api/v1/payments")
    Call<FintechPayment> createPayment(@Header("Authorization") String accessToken, @Body FintechPayment payment);

    @GET("/fintech/api/v1/payments/{externalId}")
    Call<FintechPayment> getPayment(@Header("Authorization") String accessToken,
                                    @Path("externalId") String externalId);

    @GET("/fintech/api/v1/payments/{externalId}/state")
    Call<FintechPaymentDocState> getPaymentDocState(@Header("Authorization") String accessToken,
                                                    @Path("externalId") String externalId);

    @GET("/fintech/api/v2/statement/summary")
    Call<FintechStatementSummary> getStatementSummary(@Header("Authorization") String accessToken,
                                                      @Query("accountNumber") String accountNumber,
                                                      @Query("statementDate") LocalDate statementDate);

    @GET("/fintech/api/v2/statement/transactionId")
    Call<FintechStatementTransaction> getStatementTransactionId(@Header("Authorization") String accessToken,
                                                                @Query("id") String id,
                                                                @Query("accountNumber") String accountNumber,
                                                                @Query("operationDate") LocalDate operationDate);

    @GET("/fintech/api/v2/statement/transactions")
    Call<FintechStatementTransactions> getStatementTransactions(@Header("Authorization") String accessToken,
                                                                @Query("accountNumber") String accountNumber,
                                                                @Query("statementDate") LocalDate statementDate,
                                                                @Query("page") int page,
                                                                @Query("curFormat") String curFormat);


    @POST("/fintech/api/v1/payrolls")
    Call<FintechPayroll> createPayroll(@Header("Authorization") String accessToken, @Body FintechPayroll payroll);

    @GET("/fintech/api/v1/payrolls/{externalId}")
    Call<FintechPayroll> getPayroll(@Header("Authorization") String accessToken, @Path("externalId") String externalId);

    @GET("/fintech/api/v1/payrolls/{externalId}/state")
    Call<FintechPayrollState> getPayrollState(@Header("Authorization") String accessToken, @Path("externalId") String externalId);

    @POST("/fintech/api/sbpb2b/v1/sbp/payment-link/create")
    Call<SbpB2BLinkCreateResponse> createPaymentLink(@Header("Authorization") String accessToken, @Body SbpB2BLinkCreateRequest request);

    @GET("/fintech/api/sbpb2b/v1/sbp/payment-link/getTransactionList/{linkId}")
    Call<SbpB2BgetTransactionListResponse> getPaymentLinkList(@Header("Authorization") String accessToken, @Path("linkId") String linkId);
}
