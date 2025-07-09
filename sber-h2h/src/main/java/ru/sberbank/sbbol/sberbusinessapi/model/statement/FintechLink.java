package ru.sberbank.sbbol.sberbusinessapi.model.statement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Ссылка на связанные ресурсы
 */
@Data
public class FintechLink implements Serializable {
  @JsonProperty("href")
  private String href;

  @JsonProperty("rel")
  private String rel;
}

