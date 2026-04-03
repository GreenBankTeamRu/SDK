package ru.sberbank.sbbol.sberbusinessapi.model.paymentlink;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Информация о функциональной ссылке
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Идентификатор ссылки
     */
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Некорректный формат UUID")
    @JsonProperty("linkId")
    private String linkId;

    /**
     * Направление (внутренняя или внешняя)
     */
    @JsonProperty("linkDirection")
    private String linkDirection;

    /**
     * Наименование типа ссылки (одноразовая или многоразовая)
     */
    @JsonProperty("linkTypeName")
    private String linkTypeName;

    /**
     * Идентификатор функциональной ссылки
     */
    @JsonProperty("qrcId")
    private String qrcId;
}