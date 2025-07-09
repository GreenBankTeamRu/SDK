package ru.sberbank.sbbol.sberbusinessapi.api;

import ru.sberbank.sbbol.sberbusinessapi.model.clientinfo.ClientInfoResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.AcceptanceAdvance;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.CertRequest;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.CertRequestEIO;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.CryptoInfoEIOResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.crypto.CryptoInfoResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.dictionary.DictionaryResponse;
import ru.sberbank.sbbol.sberbusinessapi.model.payment.FintechPayment;
import ru.sberbank.sbbol.sberbusinessapi.model.payment.FintechPaymentDocState;
import ru.sberbank.sbbol.sberbusinessapi.model.payroll.FintechPayroll;
import ru.sberbank.sbbol.sberbusinessapi.model.payroll.FintechPayrollState;
import ru.sberbank.sbbol.sberbusinessapi.model.statement.FintechStatementSummary;
import ru.sberbank.sbbol.sberbusinessapi.model.statement.FintechStatementTransaction;
import ru.sberbank.sbbol.sberbusinessapi.model.statement.FintechStatementTransactions;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public interface H2hApi {

    /**
     * Метод получения справочников
     *
     * @param accessToken токен авторизации
     * @param dictType    тип справочника
     * @return
     */
    DictionaryResponse getDictionary(String accessToken, String dictType);

    /**
     * Метод получения информации о пользователе
     *
     * @param accessToken токен авторизации
     * @return
     */
    ClientInfoResponse getClientInfo(String accessToken);

    /**
     * Метод получения информации о криптографии
     *
     * @param accessToken токен авторизации
     * @return
     */
    CryptoInfoResponse getCrypto(String accessToken);


    /**
     * Метод получения информации о криптографии EIO
     * @param accessToken токен авторизации
     * @return
     */
    CryptoInfoEIOResponse getCryptoEio(@NotBlank String accessToken);

    /**
     * Запрос на выпуск сертификата
     * @param accessToken токен авторизации
     * @param request объект запроса
     * @return
     */
    CertRequest certificateRequest(@NotBlank String accessToken, CertRequest request);

    /**
     * Запрос на выпуск сертификата EIO
     * @param accessToken токен авторизации
     * @param request объект запроса
     * @return
     */
    CertRequestEIO getCertRequestEio(@NotBlank String accessToken, CertRequestEIO request);

    /**
     * Запрос на активацию сертификата еио
     * @param accessToken токен авторизации
     * @param externalId идентификатор сертификата
     */
    void activateCertEIO(@NotBlank String accessToken, String externalId);
    /**
     * Запрос на активацию сертификата
     * @param accessToken токен авторизации
     * @param externalId идентификатор сертификата
     */
    void activateCert(@NotBlank String accessToken, String externalId);

    /**
     * Метод печати сертификата
     * @param accessToken токен авторизации
     * @param externalId идентификатор сертификата
     * @return
     */
    byte[] printCertificate(String accessToken, String externalId);

    /**
     * Метод получения статуса сертификата
     * @param accessToken токен авторизации
     * @param externalId идентификатор сертификата
     * @return
     */
    AcceptanceAdvance getCertState(@NotBlank String accessToken, String externalId);


    /**
     * Метод получения статуса сертификата eio
     * @param accessToken токен авторизации
     * @param externalId идентификатор сертификата
     * @return
     */
    AcceptanceAdvance getCertEIOState(@NotBlank String accessToken, String externalId);

    /**
     * Метод создания платежа
     *
     * @param accessToken токен авторизации
     * @param payment     платеж
     * @return созданный платеж
     */
    FintechPayment createPayment(String accessToken, FintechPayment payment);

    /**
     * Метод получения платежа
     *
     * @param accessToken токен авторизации
     * @param externalId  идентификатор платежа
     * @return платеж
     */
    FintechPayment getPayment(String accessToken, String externalId);

    /**
     * Метод получения статуса платежа
     *
     * @param accessToken токен авторизации
     * @param externalId  идентификатор платежа
     * @return статус платежа
     */
    FintechPaymentDocState getPaymentDocState(String accessToken, String externalId);

    /**
     * Получить информацию по оборотам счета
     * @param accessToken токен авторизации
     * @param accountNumber номер счета
     * @param statementDate дата оборота
     * @return обороты счета
     */
    FintechStatementSummary getStatementSummary(String accessToken, String accountNumber, LocalDate statementDate);

    /**
     * Получить информацию из выписки по одной операции
     * @param accessToken токен авторизации
     * @param id идентификатор оборота
     * @param accountNumber номер счета
     * @param operationDate дата оборота
     * @return оборот счета
     */
    FintechStatementTransaction getStatementTransactionId(String accessToken, String id, String accountNumber, LocalDate operationDate);

    /**
     * Получить информацию из выписки по нескольким операциям
     * @param accessToken токен авторизации
     * @param accountNumber номер счета
     * @param statementDate дата оборота
     * @param page номер страницы
     * @param curFormat формат
     * @return список операций
     */
    FintechStatementTransactions getStatementTransactions(String accessToken, String accountNumber, LocalDate statementDate, int page, String curFormat);

    /**
     * Метод создания зарплатной ведомости
     *
     * @param accessToken токен авторизации
     * @param payroll     зп ведомость
     * @return зп ведомость
     */
    FintechPayroll createPayroll(String accessToken, FintechPayroll payroll);

    /**
     * Метод получения зарплатной ведомости
     *
     * @param accessToken токен авторизации
     * @param externalId  идентификатор ведомости
     * @return ведомость
     */
    FintechPayroll getPayroll(String accessToken, String externalId);

    /**
     * Метод получения статуса ведомости
     * @param accessToken токен авторизации
     * @param externalId идентификатор ведомости
     * @return статус ведомости
     */
    FintechPayrollState getPayrollState(String accessToken, String externalId);
}
