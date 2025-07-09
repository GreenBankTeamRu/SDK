package ru.sberbank.sbbol.sberbusinessapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoBodyResponse {

    private Boolean isCorpCardHolder;
    private String orgOktmo;
    private String sub;
    private String eksEpkId;
    private String epkSflId;
    private Boolean emailConfirmed;
    private String orgKpp;
    private String iss;
    @JsonProperty("OrgName")
    private String orgName;
    private Boolean buyOnCreditMmb;
    private Integer individualExecutiveAgency;
    private String userSignatureType;
    private String orgLawFormShort;
    private Integer summOfferSmartCredit;
    @JsonProperty("HashOrgId")
    private String hashOrgId;
    private Boolean isIdentified;
    private Boolean nonClient;
    private Boolean hasActiveCreditLine;
    private String orgPprbId;
    private String email;
    private String offerExpirationDate;
    private String inn;
    private Integer active;
    private String orgJuridicalAddress;
    private String orgFullName;
    private String userGuid;
    private Boolean orgUnconfirmed;
    private String aud;
    private String orgBusinessSegment;
    private String userCryptoType;
    private String userGroups;
    private List<String> userRoles;
    private String orgOkpo;
    private Boolean sbbol3;
    private Boolean offerSmartCredit;
    private String orgOgrn;
    private String name;
    private Boolean inquiryOrder;
    private String orgLawForm;
    private String phone_number;
    private String digitalId;
    private String digitalUserId;
    private String usl;
    private List<Account> accounts;

}
