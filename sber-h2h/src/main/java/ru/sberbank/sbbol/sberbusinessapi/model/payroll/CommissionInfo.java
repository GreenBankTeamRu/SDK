package ru.sberbank.sbbol.sberbusinessapi.model.payroll;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommissionInfo {

    @JsonProperty("actualRate")
    @DecimalMin(value = "0.01", message = "Фактическая тарифная ставка должна быть больше или равна 0.01")
    private String actualRate;

    @JsonProperty("actualSum")
    @DecimalMin(value = "0.01", message = "Фактическая сумма должна быть больше или равна 0.01")
    private String actualSum;

    @JsonProperty("estimatedRate")
    @DecimalMin(value = "0.01", message = "Предварительная тарифная ставка должна быть больше или равна 0.01")
    private String estimatedRate;

    @JsonProperty("estimatedSum")
    @DecimalMin(value = "0.01", message = "Предварительная сумма должна быть больше или равна 0.01")
    private String estimatedSum;

    @JsonProperty("invoiceDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate invoiceDate;
}