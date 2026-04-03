package ru.sberbank.sbbol.sberbusinessapi.model.paymentlink;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Ответ со списком транзакций
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SbpB2BgetTransactionListResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Список ошибок (пустой массив при успешном запросе)
     */
    @JsonProperty("error")
    private List<Object> error;

    /**
     * Response entity
     */
    @JsonProperty("responseEntity")
    private ResponseEntity responseEntity;

    /**
     * Response entity
     */
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseEntity implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * Список транзакций
         */
        @Size(min = 0, max = 100, message = "Список транзакций должен содержать от 0 до 100 элементов")
        @JsonProperty("transactions")
        private List<@Valid Transaction> transactions;
    }
}