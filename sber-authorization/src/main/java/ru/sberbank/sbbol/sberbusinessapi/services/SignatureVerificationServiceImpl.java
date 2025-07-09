package ru.sberbank.sbbol.sberbusinessapi.services;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;
import ru.sberbank.sbbol.sberbusinessapi.exception.InvalidJwtException;

import java.io.FileInputStream;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getUrlDecoder;

@Slf4j
public class SignatureVerificationServiceImpl implements SignatureVerificationService {

    private final X509Certificate trustedCert;
    private static final String PROVIDER_NAME = BouncyCastleProvider.PROVIDER_NAME;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public SignatureVerificationServiceImpl(String trustedCertPath) {
        try (FileInputStream fis = new FileInputStream(trustedCertPath)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509", PROVIDER_NAME);
            this.trustedCert = (X509Certificate) cf.generateCertificate(fis);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verifyJwt(String jwt) throws InvalidJwtException {
        try {
            String[] parts = splitJwt(jwt);
            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];

            byte[] cmsSignature = decodeBase64Url(signature);
            byte[] signedData = (header + "." + payload).getBytes(UTF_8);

            if (!verifyCMSSignature(cmsSignature, signedData)) {
                throw new InvalidJwtException("Ошибка при проверке CMS подписи");
            }

            List<X509Certificate> cmsCertificates = getCertificatesFromCMS(cmsSignature);
            X509Certificate signerCert = getSignerCertificate(cmsSignature);

            return verifyCertChain(signerCert, cmsCertificates);
        } catch (Exception e) {
            throw new InvalidJwtException("Ошибка при проверке подписи JWT", e);
        }
    }

    private static String[] splitJwt(String jwt) {
        return Arrays.copyOfRange(jwt.split("\\."), 0, 3);
    }

    private static byte[] decodeBase64Url(String base64UrlEncodedString) {
        return getUrlDecoder().decode(base64UrlEncodedString);
    }

    private boolean verifyCMSSignature(byte[] cmsSignature, byte[] expectedData) throws Exception {
        CMSProcessableByteArray signedContent = new CMSProcessableByteArray(expectedData);
        CMSSignedData signedData = new CMSSignedData(signedContent, cmsSignature);
        Store certStore = signedData.getCertificates();
        Collection<SignerInformation> signers = signedData.getSignerInfos().getSigners();

        for (SignerInformation signer : signers) {
            X509Certificate cert = getCertificate(certStore, signer);
            SignerInformationVerifier verifier = createSignerInformationVerifier(cert);

            if (!signer.verify(verifier)) {
                return false;
            }
        }

        return true;
    }

    private X509Certificate getCertificate(Store certStore, SignerInformation signer) throws CertificateException {
        Collection<?> certHolders = certStore.getMatches(signer.getSID());
        X509CertificateHolder certHolder = (X509CertificateHolder) certHolders.iterator().next();
        return new JcaX509CertificateConverter()
                .setProvider(PROVIDER_NAME)
                .getCertificate(certHolder);
    }

    private SignerInformationVerifier createSignerInformationVerifier(X509Certificate cert) throws OperatorCreationException {
        return new JcaSimpleSignerInfoVerifierBuilder()
                .setProvider(PROVIDER_NAME)
                .build(cert);
    }

    private X509Certificate getSignerCertificate(byte[] cmsSignature) throws Exception {
        CMSSignedData signedData = new CMSSignedData(cmsSignature);
        Store certStore = signedData.getCertificates();
        Collection<SignerInformation> signers = signedData.getSignerInfos().getSigners();

        if (signers.isEmpty()) {
            throw new CertificateException("Не найден сертификат подписчика");
        }

        SignerInformation signer = signers.iterator().next();
        return getCertificate(certStore, signer);
    }

    private boolean verifyCertChain(X509Certificate signerCert, List<X509Certificate> cmsCertificates) throws Exception {
        ArrayList<X509Certificate> certChain = new ArrayList<>(cmsCertificates);
        if (!certChain.contains(signerCert)) {
            certChain.add(0, signerCert);
        }

        if (!certChain.contains(trustedCert)) {
            certChain.add(trustedCert);
        }

        CertPath certPath = CertificateFactory.getInstance("X.509", PROVIDER_NAME).generateCertPath(certChain);
        Set<TrustAnchor> trustAnchors = Collections.singleton(new TrustAnchor(trustedCert, null));
        PKIXParameters params = new PKIXParameters(trustAnchors);
        params.setRevocationEnabled(false);

        CertStore certStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certChain), PROVIDER_NAME);
        params.addCertStore(certStore);

        CertPathValidator.getInstance("PKIX", PROVIDER_NAME).validate(certPath, params);
        return true;
    }

    private List<X509Certificate> getCertificatesFromCMS(byte[] cmsSignature) throws Exception {
        CMSSignedData signedData = new CMSSignedData(cmsSignature);
        return signedData.getCertificates()
                .getMatches(null)
                .stream()
                .map(holder -> {
                    try {
                        return new JcaX509CertificateConverter()
                                .setProvider(PROVIDER_NAME)
                                .getCertificate((X509CertificateHolder) holder);
                    } catch (CertificateException e) {
                        throw new RuntimeException("Ошибка конвертации сертификата", e);
                    }
                })
                .collect(Collectors.toList());
    }
}