package ru.sberbank.sbbol.sberbusinessapi.model;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentalInfo {

    /**
     * Уникальный идентификатор платежа
     */
    @NotBlank
    private String uip;

    /**
     * Показатель статуса налогоплательщика (реквизит - 101)
     */
    @NotBlank
    private String drawerStatus101;

    /**
     * Код бюджетной классификации (реквизит - 104)
     */
    @NotBlank
    private String kbk;

    /**
     * Код ОКТМО (реквизит - 105)
     */
    @NotBlank
    private String oktmo;

    /**
     * Показатель основания платежа (реквизит - 106)
     */
    @NotBlank
    private String reasonCode106;

    /**
     * Налоговый период / код таможенного органа (реквизит - 107)
     */
    @NotBlank
    private String taxPeriod107;

    /**
     * Номер налогового документа (реквизит - 108)
     */
    @NotBlank
    private String docNumber108;

    /**
     * Тип налогового платежа (реквизит - 110)
     */
    @NotBlank
    private String paymentKind110;
}
