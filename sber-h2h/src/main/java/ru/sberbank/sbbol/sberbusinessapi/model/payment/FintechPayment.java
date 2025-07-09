package ru.sberbank.sbbol.sberbusinessapi.model.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FintechPayment {
    @JsonProperty("number")
    @Pattern(regexp = "^[0-9]{1,6}$")
    private String number;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate date;

    @JsonProperty("digestSignatures")
    @Valid
    private List<FintechSignature> digestSignatures = null;

    @JsonProperty("bankStatus")
    private String bankStatus;

    @JsonProperty("bankComment")
    private String bankComment;

    @JsonProperty("externalId")
    @NotNull
    private UUID externalId;

    @JsonProperty("amount")
    @NotNull
    private BigDecimal amount;

    @JsonProperty("operationCode")
    @NotNull
    @Pattern(regexp = "^01$")
    private String operationCode;

    @JsonProperty("deliveryKind")
    @Pattern(regexp = "^(электронно|срочно|0)$")
    private String deliveryKind;

    @JsonProperty("priority")
    @NotNull
    @Pattern(regexp = "^(1|2|3|4|5)$")
    private String priority;

    @JsonProperty("urgencyCode")
    @Pattern(regexp = "^(INTERTAL|INTERNAL_NOTIF|OFFHOURS|BESP|NORMAL)$")
    private String urgencyCode;

    @JsonProperty("voCode")
    @Pattern(regexp = "^[0-9]{5}$")
    private String voCode;

    @JsonProperty("purpose")
    @NotNull
    @Pattern(regexp = "^.{1,210}$")
    private String purpose;

    @JsonProperty("departmentalInfo")
    @Valid
    private FintechDepartmentalInfo departmentalInfo;

    @JsonProperty("payerName")
    @NotNull
    @Pattern(regexp = "^.{1,254}$")
    private String payerName;

    @JsonProperty("payerInn")
    @NotNull
    @Pattern(regexp = "^([0-9]{5}|[0-9]{10}|[0-9]{12}|0)$")
    private String payerInn;

    @JsonProperty("payerKpp")
    @NotNull
    @Pattern(regexp = "^([0-9]{9}|0)$")
    private String payerKpp;

    @JsonProperty("payerAccount")
    @NotNull
    @Pattern(regexp = "^[0-9]{20}$")
    private String payerAccount;

    @JsonProperty("payerBankBic")
    @NotNull
    @Pattern(regexp = "^[0-9]{9}$")
    private String payerBankBic;

    @JsonProperty("payerBankCorrAccount")
    @NotNull
    @Pattern(regexp = "^[0-9]{20}$")
    private String payerBankCorrAccount;

    @JsonProperty("payeeName")
    @NotNull
    @Pattern(regexp = "^.{1,254}$")
    private String payeeName;

    @JsonProperty("payeeInn")
    @Pattern(regexp = "^([0-9]{5}|[0-9]{10}|[0-9]{12}|0)$")
    private String payeeInn;

    @JsonProperty("payeeKpp")
    @Pattern(regexp = "^([0-9]{9}|0)$")
    private String payeeKpp;

    @JsonProperty("payeeAccount")
    @Pattern(regexp = "^[0-9]{20}$")
    private String payeeAccount;

    @JsonProperty("payeeBankBic")
    @NotNull
    @Pattern(regexp = "^[0-9]{9}$")
    private String payeeBankBic;

    @JsonProperty("payeeBankCorrAccount")
    @Pattern(regexp = "^[0-9]{20}$")
    private String payeeBankCorrAccount;

    @JsonProperty("crucialFieldsHash")
    private String crucialFieldsHash;

    @JsonProperty("vat")
    @Valid
    private FintechVat vat;

    @JsonProperty("incomeTypeCode")
    @Pattern(regexp = "^[1-9]{1,6}$")
    private String incomeTypeCode;

    @JsonProperty("isPaidByCredit")
    private Boolean isPaidByCredit;

    @JsonProperty("creditContractNumber")
    private String creditContractNumber;
}
