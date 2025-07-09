package ru.sberbank.sbbol.sberbusinessapi.model.statement;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Операции по выписке
 */
@Data
public class FintechStatementTransactions implements Serializable {
  @JsonProperty("_links")
  @Valid
  private List<FintechLink> links = null;

  @JsonProperty("transactions")
  @Valid
  private List<FintechStatementTransaction> transactions = null;

}

