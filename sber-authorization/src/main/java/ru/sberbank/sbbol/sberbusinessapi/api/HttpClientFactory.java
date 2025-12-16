package ru.sberbank.sbbol.sberbusinessapi.api;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.internal.tls.OkHostnameVerifier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import ru.sberbank.sbbol.sberbusinessapi.utils.CertUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

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
        trustStore.load(null, null);

        if (customTrustPath != null && !customTrustPath.isEmpty()) {
            Path path = Paths.get(customTrustPath);
            if (Files.isDirectory(path)) {
                try (Stream<Path> paths = Files.list(path)) {
                    paths
                            .filter(p -> p.getFileName().toString().toLowerCase().matches(".*\\.(cer|crt)$"))
                            .forEach(p -> {
                                try {
                                    loadCertificateFile(trustStore, p.toFile().getAbsolutePath());
                                } catch (Exception e) {
                                    throw new RuntimeException("Failed to load certificate: " + p, e);
                                }
                            });
                }
            } else if (Files.isRegularFile(path)) {
                loadCertificateFile(trustStore, customTrustPath);
            } else {
                throw new IllegalArgumentException("customTrustPath must be a valid file or directory: " + customTrustPath);
            }
        }

        mergeKeystores(CertUtils.getSystemTrustStore(), trustStore);
        return trustStore;
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

    private void loadCertificateFile(KeyStore trustStore, String certPath) throws Exception {
        String lowerPath = certPath.toLowerCase();
        if (lowerPath.endsWith(".pem")) {
            throw new UnsupportedOperationException("PEM format is not supported for multi-cert trust path. Use .cer/.crt or directory.");
            // (если PEM всё же нужен — можно вызвать loadPemCertificates отдельно, но он не поддерживает многофайловость)
        } else if (lowerPath.endsWith(".cer") || lowerPath.endsWith(".crt")) {
            try (InputStream fis = new FileInputStream(certPath)) {
                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(fis);
                String alias = "cert-" + certificate.getSerialNumber().toString(16);
                trustStore.setCertificateEntry(alias, certificate);
            }
        } else {
            throw new IllegalArgumentException("Unsupported certificate format: " + certPath + ". Expected .cer or .crt.");
        }
    }
}