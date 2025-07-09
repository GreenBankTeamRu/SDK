package ru.sberbank.sbbol.sberbusinessapi.model;

public enum CryptoprofileType {
    SMS("Sms"),
    TOKEN("Token");

    private final String value;

    public String getValue() {
        return value;
    }

    CryptoprofileType(String value) {
        this.value = value;
    }
}
