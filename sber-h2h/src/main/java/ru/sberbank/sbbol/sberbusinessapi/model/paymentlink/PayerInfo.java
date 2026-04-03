package ru.sberbank.sbbol.sberbusinessapi.model.paymentlink;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Информация о плательщике
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Номер счета плательщика
     */
    @Size(max = 20, message = "Номер счета не должен превышать 20 символов")
    @Pattern(regexp = "^[0-9]{20}$", message = "Номер счета должен содержать ровно 20 цифр")
    @JsonProperty("payerAccount")
    private String payerAccount;

    /**
     * ИНН плательщика
     */
    @Size(max = 12, message = "ИНН не должен превышать 12 символов")
    @Pattern(regexp = "^[0-9]{10}|[0-9]{12}$", message = "ИНН должен содержать 10 или 12 цифр")
    @JsonProperty("payerInn")
    private String payerInn;

    /**
     * КПП плательщика
     */
    @Size(max = 9, message = "КПП не должен превышать 9 символов")
    @Pattern(regexp = "^[0-9]{9}$", message = "КПП должен содержать ровно 9 цифр")
    @JsonProperty("payerKpp")
    private String payerKpp;

    /**
     * Наименование банка плательщика
     */
    @Size(max = 255, message = "Наименование банка не должно превышать 255 символов")
    @JsonProperty("payerBankName")
    private String payerBankName;

    /**
     * Наименование организации плательщика
     */
    @Size(max = 255, message = "Наименование организации не должно превышать 255 символов")
    @JsonProperty("payerLegalName")
    private String payerLegalName;
}