package ru.sberbank.sbbol.sberbusinessapi.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@UtilityClass
public class CertUtils {
    public static KeyStore getSystemTrustStore() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        String cacertsPath = System.getProperty("javax.net.ssl.trustStore");
        if (cacertsPath == null || cacertsPath.isEmpty()) {
            cacertsPath = System.getProperty("java.home") + "/lib/security/cacerts";
        }

        trustStore.load(Files.newInputStream(Paths.get(cacertsPath)), (char[])null);
        return trustStore;
    }
}
