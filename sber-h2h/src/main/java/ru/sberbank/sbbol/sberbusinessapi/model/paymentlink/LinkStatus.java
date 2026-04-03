package ru.sberbank.sbbol.sberbusinessapi.model.paymentlink;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LinkStatus {

    CREATED("CREATED"),
    ACTIVATED("ACTIVATED"),
    ERRORBYSBP("ERRORBYSBP"),
    REFUSEDBYSBP("REFUSEDBYSBP"),
    PAID("PAID"),
    EXPIRED("EXPIRED"),
    DELETED("DELETED"),
    REGISTERING("REGISTERING"),
    FRAUDDENY("FRAUDDENY"),
    ERRORBYCONTROL("ERRORBYCONTROL");

    private final String value;

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static LinkStatus fromValue(String value) {
        for (LinkStatus status : LinkStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}