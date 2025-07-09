package ru.sberbank.sbbol.sberbusinessapi.model.crypto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateInfo {
    /**
     * Признак активности
     */
    private Boolean active;
    /**
     * Сертификат (в формате base64)
     */
    private String cert;
    /**
     * Издатель
     */
    private String issuer;
    /**
     * Серийный номер
     */
    private String serialNumber;
    /**
     * Уникальный идентификатор
     */
    private String uuid;
}
