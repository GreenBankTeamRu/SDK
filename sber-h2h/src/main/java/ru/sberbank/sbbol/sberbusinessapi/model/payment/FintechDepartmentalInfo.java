package ru.sberbank.sbbol.sberbusinessapi.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
public class FintechDepartmentalInfo {
    @JsonProperty("uip")
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9]{1,25}$")
    private String uip;

    @JsonProperty("drawerStatus101")
    @NotNull
    @Pattern(regexp = "^(01|02|08|13)$")
    private String drawerStatus101;

    @JsonProperty("kbk")
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9]{1,20}$")
    private String kbk;

    @JsonProperty("oktmo")
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9]{1,11}$")
    private String oktmo;

    @JsonProperty("reasonCode106")
    @NotNull
    @Pattern(regexp = "^[0-9]{1,2}$")
    private String reasonCode106;

    @JsonProperty("taxPeriod107")
    @NotNull
    @Pattern(regexp = "^(0|[0-9]{8}|([0-9]{2}|МС|КВ|ПЛ|ГД)\\.[0-9]{2}\\.[0-9]{4})$")
    private String taxPeriod107;

    @JsonProperty("docNumber108")
    @NotNull
    @Pattern(regexp = "^[0-9]{1,15}$")
    private String docNumber108;

    @JsonProperty("docDate109")
    @NotNull
    @Pattern(regexp = "^(0|00|[0-9]{2}.[0-9]{2}.[0-9]{4})$")
    private String docDate109;

    @JsonProperty("paymentKind110")
    @Pattern(regexp = "^[0-9]{1,2}$")
    private String paymentKind110;
}
