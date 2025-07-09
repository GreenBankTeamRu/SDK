package ru.sberbank.sbbol.sberbusinessapi.model.statement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Сумма
 */
@Data
public class FintechAmount implements Serializable {

  @JsonProperty("amount")
  private BigDecimal amount;

  @JsonProperty("currencyName")
  private String currencyName;
}

