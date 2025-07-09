package ru.sberbank.sbbol.sberbusinessapi.model.payroll;


import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Amount {

    @JsonProperty("amount")
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше или равна 0.01")
    @NotNull(message = "Сумма обязательна")
    private float amount;

    @JsonProperty("currencyCode")
    @Pattern(regexp = "^[A-Z\\d]\\d{2}$", message = "Цифровой код валюты должен соответствовать формату")
    @NotNull(message = "Цифровой код валюты обязателен")
    private String currencyCode;

    @JsonProperty("currencyName")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Буквенный код валюты должен содержать ровно 3 буквы")
    @NotNull(message = "Буквенный код валюты обязателен")
    private String currencyName;
}