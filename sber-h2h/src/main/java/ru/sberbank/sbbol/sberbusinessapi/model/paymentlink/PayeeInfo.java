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
 * Информация о получателе
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayeeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Номер счета получателя
     */
    @Size(max = 20, message = "Номер счета не должен превышать 20 символов")
    @Pattern(regexp = "^[0-9]{20}$", message = "Номер счета должен содержать ровно 20 цифр")
    @JsonProperty("payeeAccount")
    private String payeeAccount;

    /**
     * ИНН получателя
     */
    @Size(max = 12, message = "ИНН не должен превышать 12 символов")
    @Pattern(regexp = "^[0-9]{10}|[0-9]{12}$", message = "ИНН должен содержать 10 или 12 цифр")
    @JsonProperty("payeeInn")
    private String payeeInn;

    /**
     * КПП получателя
     */
    @Size(max = 9, message = "КПП не должен превышать 9 символов")
    @Pattern(regexp = "^[0-9]{9}$", message = "КПП должен содержать ровно 9 цифр")
    @JsonProperty("payeeKpp")
    private String payeeKpp;

    /**
     * Наименование банка получателя
     */
    @Size(max = 255, message = "Наименование банка не должно превышать 255 символов")
    @JsonProperty("payeeBankName")
    private String payeeBankName;

    /**
     * Наименование организации получателя
     */
    @Size(max = 255, message = "Наименование организации не должно превышать 255 символов")
    @JsonProperty("payeeLegalName")
    private String payeeLegalName;
}