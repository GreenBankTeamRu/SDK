package ru.sberbank.sbbol.sberbusinessapi.model.crypto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoInfoEIOResponse {
    private String certCenterCode;
    private String certCenterNum;
    private List<String> certsCA;
    private List<CryptoUserInfo> cryptoUserInfos;
}
