package ru.sberbank.sbbol.sberbusinessapi.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PaymentInvoiceResponse {

    /**
     * Сумма платежа
     */
    @NotNull
    private BigDecimal amount;

    /**
     * Банковский комментарий к статусу платежа
     */
    private String bankComment;

    /**
     * Статус платежа
     */
    private String bankStatus;

    /**
     * Хэш критических полей платежа
     */
    private String crucialFieldsHash;

    /**
     * Дата составления платежа
     */
    private String date;

    /**
     * Вид платежа
     */
    private String deliveryKind;

    /**
     * Реквизиты налогового, таможенного или иного бюджетного платежа
     */
    private DepartmentalInfo departmentalInfo;

    /**
     * Электронные подписи по дайджесту документа
     */
    private List<Signature> digestSignatures;

    /**
     * Идентификатор документа, присвоенный партнером
     */
    @NotBlank
    private String externalId;

    /**
     * Код вида дозода получателей выплаты по 229-фз
     */
    private String incomeTypeCode;

    /**
     * Номер документа
     */
    private String number;

    /**
     * Код операции
     */
    @NotBlank
    private String operationCode;

    /**
     * Счет получателя платежа
     */
    @NotBlank
    private String payeeAccount;

    /**
     * БИК получателя платежа
     */
    @NotBlank
    private String payeeBankBic;

    /**
     * Корсчет банка получателя платежа
     */
    private String payeeBankCorrAccount;

    /**
     * ИНН получателя платежа
     */
    private String payeeInn;

    /**
     * КПП получателя платежа
     */
    private String payeeKpp;

    /**
     * Полное наименования получателя платежа
     */
    @NotBlank
    private String payeeName;

    /**
     * Счет плательщика платежа
     */
    @NotBlank
    private String payerAccount;

    /**
     * БИК плательщика платежа
     */
    @NotBlank
    private String payerBankBic;

    /**
     * Корсчет банка плательщика платежа
     */
    @NotBlank
    private String payerBankCorrAccount;

    /**
     * ИНН плательщика платежа
     */
    @NotBlank
    private String payerInn;

    /**
     * КПП плательщика платежа
     */
    private String payerKpp;

    /**
     * Полное наименование плательщика платежа
     */
    @NotBlank
    private String payerName;

    /**
     * Очередность платежа
     */
    @NotBlank
    private String priority;

    /**
     * Назначение платежа
     */
    @NotBlank
    private String purpose;

    /**
     * Код срочности
     */
    private String urgencyCode;

    /**
     * Данные НДС
     */
    private Vat vat;

    /**
     * Код вида валютной операции
     */
    private String voCode;
}