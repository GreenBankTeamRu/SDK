package ru.sberbank.sbbol.sberbusinessapi.model.statement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets FintechTransactionDirection
 */
public enum FintechTransactionDirection {
  
  DEBIT("DEBIT"),
  
  CREDIT("CREDIT");

  private String value;

  FintechTransactionDirection(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static FintechTransactionDirection fromValue(String value) {
    for (FintechTransactionDirection b : FintechTransactionDirection.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

