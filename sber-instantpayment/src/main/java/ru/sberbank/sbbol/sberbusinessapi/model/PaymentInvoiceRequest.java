package ru.sberbank.sbbol.sberbusinessapi.model;

import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class PaymentInvoiceRequest  extends PaymentInvoiceBaseRequest {
    /**
     * Идентификатор получателя платежа
     */
    @Pattern(regexp = "^[0-9a-f]{64}$")
    private String payeeOrgIdHash;
}
