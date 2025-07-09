package ru.sberbank.sbbol.sberbusinessapi.model.statement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Данные операции по рублевой выписке
 */
@Data
public class FintechStatementTransaction implements Serializable {
  @JsonProperty("amount")
  private FintechAmount amount;

  @JsonProperty("amountRub")
  private FintechAmount amountRub;

  @JsonProperty("correspondingAccount")
  private String correspondingAccount;

  @JsonProperty("direction")
  private FintechTransactionDirection direction;

  @JsonProperty("documentDate")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate documentDate;

  @JsonProperty("filial")
  private String filial;

  @JsonProperty("number")
  private String number;

  @JsonProperty("operationCode")
  private String operationCode;

  @JsonProperty("operationDate")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime operationDate;

  @JsonProperty("paymentPurpose")
  private String paymentPurpose;

  @JsonProperty("priority")
  private String priority;

  @JsonProperty("revaln")
  private String revaln;

  @JsonProperty("uuid")
  private UUID uuid;

  @JsonProperty("rurTransfer")
  private FintechRURTransfer rurTransfer;

  @JsonProperty("curTransfer")
  private FintechCurTransfer curTransfer;

  @JsonProperty("transactionId")
  private Long transactionId;

  @JsonProperty("operationId")
  private String operationId;

  @JsonProperty("swiftTransfer")
  private FintechSWIFTTransfer swiftTransfer;

}

