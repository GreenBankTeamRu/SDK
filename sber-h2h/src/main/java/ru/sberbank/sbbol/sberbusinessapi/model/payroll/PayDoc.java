package ru.sberbank.sbbol.sberbusinessapi.model.payroll;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayDoc {

    @JsonProperty("amount")
    @NotNull(message = "Сумма обязательна")
    @Valid
    private Amount amount;

    @JsonProperty("docDate")
    @NotNull(message = "Дата расчетного документа обязательна")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate docDate;

    @JsonProperty("number")
    @NotBlank(message = "Номер расчетного документа обязателен")
    private String number;

    @JsonProperty("payeeAccount")
    @Pattern(regexp = "^[0-9]{20}$", message = "Номер счета получателя должен содержать ровно 20 цифр")
    @NotNull(message = "Номер счета получателя обязателен")
    private String payeeAccount;

    @JsonProperty("payeeBic")
    @Pattern(regexp = "^[0-9]{9}$", message = "БИК банка получателя должен содержать ровно 9 цифр")
    @NotNull(message = "БИК банка получателя обязателен")
    private String payeeBic;

    @JsonProperty("payerAccount")
    @Pattern(regexp = "^[0-9]{20}$", message = "Номер счета плательщика должен содержать ровно 20 цифр")
    @NotNull(message = "Номер счета плательщика обязателен")
    private String payerAccount;

    @JsonProperty("payerBic")
    @Pattern(regexp = "^[0-9]{9}$", message = "БИК банка плательщика должен содержать ровно 9 цифр")
    @NotNull(message = "БИК банка плательщика обязателен")
    private String payerBic;

    @JsonProperty("purpose")
    @NotBlank(message = "Назначение платежа обязательно")
    private String purpose;
}