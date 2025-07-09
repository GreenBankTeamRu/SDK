package ru.sberbank.sbbol.sberbusinessapi.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentInvoiceBudgetRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentInvoiceFromAnyRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentInvoiceRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentInvoiceResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentState;

interface InstantPaymentApiService {

    @POST("/fintech/api/v1/payments/from-invoice")
    Call<PaymentInvoiceResponse> getPaymentInvoice(@Header("Authorization") String accessToken, @Body PaymentInvoiceRequest request);

    @POST("/fintech/api/v1/payments/from-invoice-budget")
    Call<PaymentInvoiceResponse> getPaymentInvoiceBudget(@Header("Authorization") String accessToken, @Body PaymentInvoiceBudgetRequest request);

    @POST("/fintech/api/v1/payments/from-invoice-any")
    Call<PaymentInvoiceResponse> getPaymentInvoiceAny(@Header("Authorization") String accessToken, @Body PaymentInvoiceFromAnyRequest request);

    @GET("/fintech/api/v1/payments/{externalId}/state")
    Call<PaymentState> getPaymentState(
            @Header("Authorization") String accessToken,
            @Path("externalId") String externalId,
            @Header("Accept") String accept
    );

    @GET("/fintech/api/v1/payments/{externalId}")
    Call<PaymentInvoiceResponse> getPayment(
            @Header("Authorization") String accessToken,
            @Path("externalId") String externalId,
            @Header("Accept") String accept
    );
}
