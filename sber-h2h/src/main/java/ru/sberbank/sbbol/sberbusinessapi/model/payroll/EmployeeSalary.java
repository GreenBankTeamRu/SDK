package ru.sberbank.sbbol.sberbusinessapi.model.payroll;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSalary {

    @JsonProperty("account")
    @Pattern(regexp = "^[0-9]{20}$", message = "Номер счета должен содержать ровно 20 цифр")
    @NotNull(message = "Номер счета сотрудника обязателен")
    private String account;

    @JsonProperty("amount")
    @NotNull(message = "Сумма обязательна")
    @Valid
    private Amount amount;

    @JsonProperty("bankMessage")
    private String bankMessage;

    @JsonProperty("bic")
    @Pattern(regexp = "^[0-9]{9}$", message = "БИК должен содержать ровно 9 цифр")
    private String bic;

    @JsonProperty("firstName")
    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @JsonProperty("lastName")
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @JsonProperty("middleName")
    private String middleName;

    @JsonProperty("receiptResult")
    private String receiptResult;

    @JsonProperty("receiptStatus")
    private String receiptStatus;

    @JsonProperty("result")
    private String result;

    @JsonProperty("withheldAmount")
    private BigDecimal withheldAmount;
}