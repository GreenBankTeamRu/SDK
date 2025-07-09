package ru.sberbank.sbbol.sberbusinessapi.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class PaymentInvoiceBudgetRequest extends PaymentInvoiceRequest {

    /**
     * Реквизиты налогового, таможенного или иного бюджетного платежа
     */
    @NotNull
    private DepartmentalInfo departmentalInfo;

    /**
     * БИК банка получателя платежа
     */
    @NotBlank
    @Pattern(regexp = "^[0-9]{9}$")
    private String payeeBankBic;

    /**
     * Кор. счет банка получателя платежа
     */
    @Pattern(regexp = "^[0-9]{20}$")
    private String payeeBankCorrAccount;

    /**
     * ИНН получателя платежа
     */
    @Pattern(regexp = "^([0-9]{5}|[0-9]{10}|[0-9]{12}|0)$")
    @NotBlank
    private String payeeInn;

    /**
     * ИНН получателя платежа
     */
    @Pattern(regexp = "^([0-9]{9}|0)$")
    private String payeeKpp;

    /**
     * Наименование получателя платежа
     */
    @Pattern(regexp = "^.{0,254}$")
    @NotBlank
    private String payeeName;


}
