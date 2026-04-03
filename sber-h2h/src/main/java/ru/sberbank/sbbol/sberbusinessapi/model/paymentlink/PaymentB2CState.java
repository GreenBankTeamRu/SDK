package ru.sberbank.sbbol.sberbusinessapi.model.paymentlink;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Статус платежа B2C
 */
@Getter
@AllArgsConstructor
public enum PaymentB2CState {

    @JsonProperty("ACCEPTED")
    ACCEPTED("ACCEPTED"),

    @JsonProperty("REFUSEDBYBANK")
    REFUSEDBYBANK("REFUSEDBYBANK"),

    @JsonProperty("REFUSEDBYFTS")
    REFUSEDBYFTS("REFUSEDBYFTS"),

    @JsonProperty("FRAUDDENY")
    FRAUDDENY("FRAUDDENY"),

    @JsonProperty("IMPLEMENTED")
    IMPLEMENTED("IMPLEMENTED"),

    @JsonProperty("CANCELLED")
    CANCELLED("CANCELLED");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static PaymentB2CState fromValue(String value) {
        for (PaymentB2CState state : PaymentB2CState.values()) {
            if (state.value.equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}