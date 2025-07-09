package ru.sberbank.sbbol.sberbusinessapi.model.payroll;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sberbank.sbbol.sberbusinessapi.model.payment.FintechSignature;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FintechPayroll {

    @JsonProperty("bankComment")
    private String bankComment;

    @JsonProperty("bankStatus")
    private String bankStatus;

    @JsonProperty("date")
    @NotNull(message = "Дата составления документа обязательна")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonProperty("digestSignatures")
    @Valid
    private List<FintechSignature> digestSignatures;

    @JsonProperty("number")
    private String number;

    @JsonProperty("account")
    @Pattern(regexp = "^[0-9]{20}$", message = "Номер счета должен содержать ровно 20 цифр")
    private String account;

    @JsonProperty("admissionValue")
    @Pattern(regexp = "^[0-9]{2}$", message = "Вид зачисления должен содержать ровно 2 цифры")
    @NotNull(message = "Вид зачисления обязателен")
    private String admissionValue;

    @JsonProperty("amount")
    @NotNull(message = "Сумма обязательна")
    @Valid
    private Amount amount;

    @JsonProperty("authPersonName")
    @Size(max = 60, message = "ФИО должно быть не более 60 символов")
    private String authPersonName;

    @JsonProperty("authPersonTelfax")
    @Size(max = 40, message = "Телефон/факс должен быть не более 40 символов")
    private String authPersonTelfax;

    @JsonProperty("bic")
    @Pattern(regexp = "^[0-9]{9}$", message = "БИК должен содержать ровно 9 цифр")
    @NotNull(message = "БИК обязателен")
    private String bic;

    @JsonProperty("commissionInfo")
    @Valid
    private CommissionInfo commissionInfo;

    @JsonProperty("contractDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Дата договора обязательна")
    private LocalDate contractDate;

    @JsonProperty("contractNumber")
    @NotBlank(message = "Номер договора обязателен")
    private String contractNumber;

    @JsonProperty("employeeSalaries")
    @Size(min = 1, message = "Должен быть хотя бы один сотрудник")
    @NotNull(message = "Список сотрудников обязателен")
    @Valid
    private List<EmployeeSalary> employeeSalaries;

    @JsonProperty("employeesNumber")
    @Min(1)
    @NotNull(message = "Количество сотрудников обязательно")
    private Integer employeesNumber;

    @JsonProperty("externalId")
    @NotNull(message = "Внешний идентификатор документа обязателен")
    private UUID externalId;

    @JsonProperty("incomeTypeCode")
    @Pattern(regexp = "^([1-9]{1}|[1-9]{1}[0-9]{1})$", message = "Код вида дохода должен соответствовать формату")
    private String incomeTypeCode;

    @JsonProperty("loanAmount")
    @Valid
    private Amount loanAmount;

    @JsonProperty("loanDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate loanDate;

    @JsonProperty("loanNumber")
    @Size(max = 50, message = "Номер кредитного договора должен быть не более 50 символов")
    private String loanNumber;

    @JsonProperty("month")
    @Size(min = 1, max = 50, message = "Месяц должен быть от 1 до 50 символов")
    @NotNull(message = "Месяц обязателен")
    private String month;

    @JsonProperty("orgName")
    @Size(min = 1, max = 160, message = "Наименование организации должно быть от 1 до 160 символов")
    @NotNull(message = "Наименование организации обязательно")
    private String orgName;

    @JsonProperty("orgTaxNumber")
    @Pattern(regexp = "^([0-9]{5}|[0-9]{10}|[0-9]{12}|0)$", message = "ИНН должен соответствовать формату")
    @NotNull(message = "ИНН обязателен")
    private String orgTaxNumber;

    @JsonProperty("payDocs")
    @Valid
    private List<PayDoc> payDocs;

    @JsonProperty("year")
    @Size(min = 4, max = 4, message = "Год должен быть ровно 4 символа")
    @NotNull(message = "Год обязателен")
    private String year;
}