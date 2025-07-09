package ru.sberbank.sbbol.sberbusinessapi.model.crypto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoInfoResponse {
    private String certBank;
    private String certBankUuid;
    private String certCenterCode;
    private String certCenterNum;
    private List<String> certsCA;
    private List<CryptoProfileInfo> cryptoProfileInfos;
}
