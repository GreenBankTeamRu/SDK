package ru.sberbank.sbbol.sberbusinessapi.model.statement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sberbank.sbbol.sberbusinessapi.model.payment.FintechDepartmentalInfo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Данные рублевой операции
 */
public class FintechRURTransfer  implements Serializable {
  @JsonProperty("cartInfo")
  private FintechCartInfo cartInfo;

  @JsonProperty("deliveryKind")
  private String deliveryKind;

  @JsonProperty("departmentalInfo")
  private FintechDepartmentalInfo departmentalInfo;

  @JsonProperty("payeeAccount")
  private String payeeAccount;

  @JsonProperty("payeeBankBic")
  private String payeeBankBic;

  @JsonProperty("payeeBankCorrAccount")
  private String payeeBankCorrAccount;

  @JsonProperty("payeeBankName")
  private String payeeBankName;

  @JsonProperty("payeeInn")
  private String payeeInn;

  @JsonProperty("payeeKpp")
  private String payeeKpp;

  @JsonProperty("payeeName")
  private String payeeName;

  @JsonProperty("payerAccount")
  private String payerAccount;

  @JsonProperty("payerBankBic")
  private String payerBankBic;

  @JsonProperty("payerBankCorrAccount")
  private String payerBankCorrAccount;

  @JsonProperty("payerBankName")
  private String payerBankName;

  @JsonProperty("payerInn")
  private String payerInn;

  @JsonProperty("payerKpp")
  private String payerKpp;

  @JsonProperty("payerName")
  private String payerName;

  @JsonProperty("payingCondition")
  private String payingCondition;

  @JsonProperty("purposeCode")
  private String purposeCode;

  @JsonProperty("receiptDate")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate receiptDate;

  @JsonProperty("valueDate")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate valueDate;

}

