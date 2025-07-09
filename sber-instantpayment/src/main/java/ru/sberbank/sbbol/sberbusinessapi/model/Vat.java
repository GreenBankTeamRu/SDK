package ru.sberbank.sbbol.sberbusinessapi.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vat {

    /**
     * Сумма НДС
     */
    private Float amount;

    /**
     * Ставка НДС
     */
    @Pattern(regexp = "^[0-9]{0,2}$")
    private String rate;

    /**
     * Способ расчета НДС.
     *
     * INCLUDED - НДС включен в сумму платежа
     * NO_VAT - не облагается НДС
     * MANUAL - ручной ввод НДС
     */
    @Pattern(regexp = "^(INCLUDED|NO_VAT|MANUAL)$")
    @NotBlank
    private String type;
}
