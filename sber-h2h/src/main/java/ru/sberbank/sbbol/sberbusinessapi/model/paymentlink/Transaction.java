package ru.sberbank.sbbol.sberbusinessapi.model.paymentlink;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Транзакция
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Уникальный идентификатор платёжного поручения
     */
    @NotNull(message = "Идентификатор поручения обязателен")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Некорректный формат UUID")
    @JsonProperty("docguid")
    private String docguid;

    /**
     * Дата платежа
     */
    @NotNull(message = "Дата платежа обязательна")
    @JsonProperty("paymentDate")
    private String paymentDate;

    /**
     * Сумма платежа в копейках
     */
    @NotNull(message = "Сумма платежа обязательна")
    @DecimalMin(value = "0", message = "Сумма не может быть отрицательной")
    @JsonProperty("amount")
    private BigDecimal amount;

    /**
     * Сумма НДС в копейках
     */
    @DecimalMin(value = "0", message = "Сумма НДС не может быть отрицательной")
    @JsonProperty("totalTaxAmount")
    private BigDecimal totalTaxAmount;

    /**
     * Идентификатор операции ОПКЦ
     */
    @Size(max = 50, message = "Идентификатор операции не должен превышать 50 символов")
    @JsonProperty("bizMsgId")
    private String bizMsgId;

    /**
     * Статус платежа
     */
    @JsonProperty("statusInfo")
    private StatusInfo statusInfo;

    /**
     * Информация о ссылке
     */
    @JsonProperty("linkInfo")
    private LinkInfo linkInfo;

    /**
     * Код типа платежа
     */
    @JsonProperty("payTypeCode")
    private String payTypeCode;

    /**
     * Назначение платежа
     */
    @Size(max = 255, message = "Назначение платежа не должно превышать 255 символов")
    @JsonProperty("paymentPurpose")
    private String paymentPurpose;

    /**
     * Номер платежа
     */
    @Size(max = 20, message = "Номер платежа не должен превышать 20 символов")
    @JsonProperty("number")
    private String number;

    /**
     * Информация о плательщике
     */
    @JsonProperty("payerInfo")
    private PayerInfo payerInfo;

    /**
     * Информация о получателе
     */
    @JsonProperty("payeeInfo")
    private PayeeInfo payeeInfo;

    /**
     * Сумма комиссии в рублях
     */
    @DecimalMin(value = "0", message = "Комиссия не может быть отрицательной")
    @JsonProperty("comission")
    private BigDecimal comission;

    /**
     * Статус платежа
     */
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * Код статуса платежа
         */
        @Size(max = 30, message = "Код статуса не должен превышать 30 символов")
        @JsonProperty("code")
        private String code;

        /**
         * Наименование статуса платежа
         */
        @Size(max = 100, message = "Наименование статуса не должно превышать 100 символов")
        @JsonProperty("name")
        private String name;
    }
}