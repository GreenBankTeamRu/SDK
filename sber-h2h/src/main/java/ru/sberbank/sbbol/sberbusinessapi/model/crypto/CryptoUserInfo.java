package ru.sberbank.sbbol.sberbusinessapi.model.crypto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoUserInfo {
    private String sub;
    private List<CryptoProfileInfo> cryptoProfileInfos;
}
