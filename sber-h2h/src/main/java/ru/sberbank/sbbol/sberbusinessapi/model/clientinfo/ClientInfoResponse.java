package ru.sberbank.sbbol.sberbusinessapi.model.clientinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfoResponse {
    private List<Account> accounts;
    private List<Address> addresses;
    private Branch branch;
    private List<DboContract> dboContracts;
    private String fullName;
    private String inn;
    private List<String> kpps;
    private String ogrn;
    private String okato;
    private String okpo;
    private String orgForm;
    private OrgKindActivity orgKindActivityInfo;
    private String orgPprbId;
    private String orgRegDateINN;
    private String orgRegDateOGRN;
    private Boolean nonClient;
    private String orgUnconfirmed;
    private String eksEpkId;
    private Boolean resident;
    private Boolean isCorpCardHolder;
    private Boolean hasActiveCreditLine;
    private String shortName;
    private String territorialBank;
    private String creditLineAvailableSum;
}
