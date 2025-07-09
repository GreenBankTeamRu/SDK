package ru.sberbank.sbbol.sberbusinessapi.model.crypto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CertRequest {
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}$")
    private String email;

    @NotNull
    private String externalId;

    @NotNull
    private String number;

    private String orgName;

    @NotNull
    private  Pkcs10 pkcs10;

    @NotNull
    private String userName;

    @NotNull
    private String userPosition;

    private String bankStatus;

    private String bankComment;
}
