package ru.sberbank.sbbol.sberbusinessapi.model.crypto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoProfileInfo {
    /**
     * Псевдоним
     */
    private String alias;
    /**
     * Данные о сертификате
     */
    private List<CertificateInfo> certificateInfos;
    /**
     * Наименование типа
     */
    private String typeName;
}
