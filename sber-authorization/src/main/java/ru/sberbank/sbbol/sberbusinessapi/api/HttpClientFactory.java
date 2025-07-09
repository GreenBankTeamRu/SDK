package ru.sberbank.sbbol.sberbusinessapi.api;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.internal.tls.OkHostnameVerifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import ru.sberbank.sbbol.sberbusinessapi.utils.CertUtils;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertPathValidator;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Builder(builderMethodName = "of")
public class HttpClientFactory {
    private static final int DEFAULT_CONNECT_TIMEOUT = 60; // seconds
    private static final int DEFAULT_READ_TIMEOUT = 60; // seconds

    @NonNull
    @Getter
    private final String host;
    private final String customCertPath;
    private final String customCertPassword;
    private final String customTrustPath;
    private final Integer connectTimeout;
    private final Integer readTimeout;
    @Getter
    private final Boolean isEnableLogs;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public OkHttpClient createHttpClient() {
        try {
            SSLContext sslContext = createSslContext();
            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), createTrustManager())
                    .hostnameVerifier(OkHostnameVerifier.INSTANCE)
                    .connectTimeout(Optional.ofNullable(connectTimeout).orElse(DEFAULT_CONNECT_TIMEOUT), TimeUnit.SECONDS)
                    .readTimeout(Optional.ofNullable(readTimeout).orElse(DEFAULT_READ_TIMEOUT), TimeUnit.SECONDS)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create OkHttpClient", e);
        }
    }

    private SSLContext createSslContext() throws Exception {
        KeyStore keyStore = loadKeyStore();
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, getPassword());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(loadSberTrustStore());

        // Настройка PKIXParameters для проверки отзыва
        PKIXParameters params = new PKIXParameters(loadSberTrustStore());
        params.setRevocationEnabled(true); // Включение проверки отзыва

        CertPathValidator validator = CertPathValidator.getInstance("PKIX");
        validator.validate(CertificateFactory.getInstance("X.509").generateCertPath(Collections.emptyList()), params);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        return sslContext;
    }

    private X509TrustManager createTrustManager() throws Exception {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore) null);

        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                return (X509TrustManager) tm;
            }
        }
        throw new IllegalStateException("No X509TrustManager found");
    }

    private KeyStore loadKeyStore() throws Exception {
        validateCustomCertPath(customCertPath);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream fis = new FileInputStream(customCertPath)) {
            keyStore.load(fis, getPassword());
        }
        return keyStore;
    }

    private KeyStore loadSberTrustStore() throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null); // Initialize empty keystore

        if (customTrustPath != null && !customTrustPath.isEmpty()) {
            if (customTrustPath.endsWith(".pem")) {
                loadPemCertificates(trustStore);
            } else if (customTrustPath.endsWith(".cer")) {
                loadCerCertificate(trustStore);
            } else {
                throw new IllegalArgumentException("Unsupported certificate format. Expected .pem or .cer.");
            }
        }

        mergeKeystores(CertUtils.getSystemTrustStore(), trustStore);
        return trustStore;
    }

    private void loadPemCertificates(KeyStore trustStore) throws Exception {
        try (FileReader reader = new FileReader(customTrustPath);
             PEMParser pemParser = new PEMParser(reader)) {

            Object pemObject;
            while ((pemObject = pemParser.readObject()) != null) {
                if (pemObject instanceof X509CertificateHolder) {
                    X509Certificate certificate = new JcaX509CertificateConverter()
                            .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                            .getCertificate((X509CertificateHolder) pemObject);

                    trustStore.setCertificateEntry("cert-" + certificate.getSerialNumber(), certificate);
                }
            }
        }
    }

    private void loadCerCertificate(KeyStore trustStore) throws Exception {
        try (InputStream fis = new FileInputStream(customTrustPath)) {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(fis);

            trustStore.setCertificateEntry("cert-" + certificate.getSerialNumber(), certificate);
        }
    }

    private void mergeKeystores(KeyStore source, KeyStore destination) throws Exception {
        Enumeration<String> aliases = source.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (source.isCertificateEntry(alias)) {
                Certificate cert = source.getCertificate(alias);
                destination.setCertificateEntry(alias, cert);
            }
        }
    }

    private char[] getPassword() {
        return Optional.ofNullable(customCertPassword).map(String::toCharArray).orElse(null);
    }

    private void validateCustomCertPath(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Custom certificate path cannot be blank or empty.");
        }
    }
}