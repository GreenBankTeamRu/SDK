package ru.sberbank.sbbol.sberbusinessapi.model.statement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Дополнительная информация о документе (картотека)
 */
@Data
public class FintechCartInfo implements Serializable {
  @JsonProperty("documentCode")
  private String documentCode;

  @JsonProperty("documentContent")
  private String documentContent;

  @JsonProperty("documentDate")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate documentDate;

  @JsonProperty("documentNumber")
  private String documentNumber;

  @JsonProperty("paymentNumber")
  private String paymentNumber;

  @JsonProperty("restAmount")
  private BigDecimal restAmount;
}

