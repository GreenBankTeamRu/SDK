package ru.sberbank.sbbol.sberbusinessapi.model.crypto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Pkcs10 {
    @NotNull
    private String bicryptId;
    @NotNull
    private String cms;
}
