package ru.sberbank.sbbol.sberbusinessapi.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class PaymentInvoiceBaseRequest extends SbbapiBaseRequest {
    /**
     * Идентификатор документа, присвоенный партнером
     */
    @NotBlank
    private String externalId;

    /**
     * Сумма платежа
     */
    @NotNull
    private Float amount;

    /**
     * Дата составления документа
     */
    @NotBlank
    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}")
    private String date;

    /**
     * Назначение платежа
     */
    @Size(max = 240)
    private String purpose;

    /**
     * Счет получателя платежа
     */
    @NotBlank
    private String payeeAccount;

    /**
     * Код срочности.
     *
     * INTERNAL - срочный
     * INTERNAL_NOTIF - срочный платеж с уведомлением
     * OFFHOURS - неотложный
     * BESP - банковские электронные срочные платежи
     * NORMAL - срочность не указана
     */
    @Pattern(regexp = "^(INTERTAL|INTERNAL_NOTIF|OFFHOURS|BESP|NORMAL)$")
    private String urgencyCode;

    /**
     * Номер платежного поручения
     */
    @Pattern(regexp = "^[0-9]{1,6}$")
    private String paymentNumber;

    /**
     * Вид платежа
     */
    @Pattern(regexp = "^(электронно|срочно|0)")
    private String deliveryKind;

    /**
     * Дата истечения заказа (платеж должен быть подтвержден клиентом)
     */
    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}")
    private String expirationDate;

    /**
     * Код операции
     */
    @Pattern(regexp = "^01")
    private String operationCode;

    /**
     * Связанные документы
     */
    private List<LinkedDoc> linkedDocs;

    /**
     * Номер заказа
     */
    private String orderNumber;

    /**
     * Очередность платежа
     */
    @Pattern(regexp = "^[4-5]")
    private String priority;

    /**
     * Данные НДС
     */
    private Vat vat;
}
