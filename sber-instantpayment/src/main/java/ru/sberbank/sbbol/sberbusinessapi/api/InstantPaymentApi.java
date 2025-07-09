package ru.sberbank.sbbol.sberbusinessapi.api;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import ru.sberbank.sbbol.sberbusinessapi.model.CryptoprofileType;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentInvoiceBudgetRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentInvoiceFromAnyRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentInvoiceRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentInvoiceResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.PaymentState;

public interface InstantPaymentApi {

    /**
     * Получение денежных средств на счет вашей компании в Сбербанке
     * @param accessToken Токен доступа
     * @param request Параметры запроса
     * @return Результат операции
     */
    PaymentInvoiceResponse createPaymentInvoice(@NotBlank String accessToken, @Valid PaymentInvoiceRequest request);

    /**
     * Оплата налоговых, таможенных и других бюджетных платежей
     * @param accessToken Токен доступа
     * @param request Параметры запроса
     * @return Результат операции
     */
    PaymentInvoiceResponse createPaymentInvoiceBudget(@NotBlank String accessToken, @Valid PaymentInvoiceBudgetRequest request);

    /**
     * Перевод, где отправитель — любая компания со счетом в Сбербанке, а получатель — любая компания со счетом в любом банке
     * @param accessToken Токен доступа
     * @param request Параметры запроса
     * @return Результат операции
     */
    PaymentInvoiceResponse createPaymentInvoiceAny(@NotBlank String accessToken, @Valid PaymentInvoiceFromAnyRequest request);

    /**
     * Метод формирования ссылки на подписание (оплату)
     * @param externalId Идентификатор черновика платежного поручения
     * @param backUrl Ссылка по которой вернуть пользователя после процедуры подписания РПП.
     * @param cryptoprofileType Тип криптопровайдера
     * @param host Сервер, на котором будет выполняться процедура подписания РПП
     * @param isProd Стенд развертывания true - production, false - dev (Если указывается host, можно не указывать)
     * @return Ссылка на подписание
     */
    String buildPaymentUrl(@NotBlank String externalId, @NotBlank String backUrl, @NotBlank CryptoprofileType cryptoprofileType, String host, Boolean isProd);

    /**
     * Метод проверки статуса ранее созданного платежного поручения
     * @param accessToken Access token пользователя, полученный через SSO.
     * @param accept Accept header. По дефолту "application/json"
     * @return информация о статусе платежного поручения
     */
    PaymentState getPaymentState(@NotBlank String accessToken, @NotBlank String externalId, String accept);

    /**
     * Метод получения информации о платежном поручении
     * @param accessToken Access token пользователя, полученный через SSO.
     * @param accept Accept header. По дефолту "application/json"
     * @return информация о платежном поручении
     */
    PaymentInvoiceResponse getPayment(@NotBlank String accessToken, @NotBlank String externalId, String accept);
}
