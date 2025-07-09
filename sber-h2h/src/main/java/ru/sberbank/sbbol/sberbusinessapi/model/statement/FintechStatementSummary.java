package ru.sberbank.sbbol.sberbusinessapi.model.statement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сводная информация по выписке
 */
@Data
public class FintechStatementSummary implements Serializable {
  @JsonProperty("closingBalance")
  private FintechAmount closingBalance;

  @JsonProperty("closingBalanceRub")
  private FintechAmount closingBalanceRub;

  @JsonProperty("composedDateTime")
  private String composedDateTime;

  @JsonProperty("creditTransactionsNumber")
  private Integer creditTransactionsNumber;

  @JsonProperty("creditTurnover")
  private FintechAmount creditTurnover;

  @JsonProperty("creditTurnoverRub")
  private FintechAmount creditTurnoverRub;

  @JsonProperty("debitTransactionsNumber")
  private Integer debitTransactionsNumber;

  @JsonProperty("debitTurnover")
  private FintechAmount debitTurnover;

  @JsonProperty("debitTurnoverRub")
  private FintechAmount debitTurnoverRub;

  @JsonProperty("lastMovementDate")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate lastMovementDate;

  @JsonProperty("openingBalance")
  private FintechAmount openingBalance;

  @JsonProperty("openingBalanceRub")
  private FintechAmount openingBalanceRub;

  @JsonProperty("openingRate")
  private BigDecimal openingRate;
}

