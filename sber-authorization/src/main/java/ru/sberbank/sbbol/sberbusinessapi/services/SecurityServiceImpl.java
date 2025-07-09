package ru.sberbank.sbbol.sberbusinessapi.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityServiceImpl implements SecurityService {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Base64.Encoder URL_SAFE_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final char[] ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~".toCharArray();
    private static final int MIN_CODE_LENGTH = 43;
    private static final int MAX_CODE_LENGTH = 128;

//    @Override
//    public String generatePKCECodeVerifier(int codeLength) {
//        validateCodeLength(codeLength);
//        return encode2Base64Url(generateRandomString(codeLength).getBytes(StandardCharsets.UTF_8));
//    }

//    @Override
//    public String generatePKCECodeVerifier() {
////        int randomLength = RANDOM.nextInt(MAX_CODE_LENGTH - MIN_CODE_LENGTH + 1) + MIN_CODE_LENGTH;
//        //todo Сейчас на финтехе баг - регулярка ограничивает длину, поэтому будем принудительно генерить с длиной 43.
//        int randomLength = 43;
//        return encode2Base64Url(generateRandomString(randomLength).getBytes(StandardCharsets.UTF_8));
//    }

    @Override
    public String generatePKCECodeVerifier() {
        byte[] randomBytes = new byte[32];
        RANDOM.nextBytes(randomBytes);
        return URL_SAFE_ENCODER.encodeToString(randomBytes);
    }

    @Override
    public String generatePKCECodeChallenge(String pKCECodeVerifier) {
        return new String(generatePKCECodeChallengeFromVerifier(pKCECodeVerifier).getBytes(StandardCharsets.UTF_8));
    }


//    @Override
//    public String generatePKCECodeChallenge() {
//        String codeVerifier = generatePKCECodeVerifier();
//        return encode2Base64Url(generatePKCECodeChallengeFromVerifier(codeVerifier).getBytes(StandardCharsets.UTF_8));
//    }

    /**
     * Генерирует код вызова PKCE на основе переданного кода верификатора.
     *
     * @param codeVerifier Код верификатора, используемый для генерации кода вызова.
     * @return Строку, представляющую собой код вызова PKCE в формате base64url.
     */
    private String generatePKCECodeChallengeFromVerifier(String codeVerifier) {
        try {
            byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes);
            byte[] digest = md.digest();
            return URL_SAFE_ENCODER.encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Не удалось найти алгоритм SHA-256.", e);
        }
    }

    private void validateCodeLength(int codeLength) {
        if (codeLength < MIN_CODE_LENGTH || codeLength > MAX_CODE_LENGTH) {
            throw new IllegalArgumentException("Длина кода верификатора должна быть в пределах от " + MIN_CODE_LENGTH + " до " + MAX_CODE_LENGTH + " символов.");
        }
    }

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALLOWED_CHARS[RANDOM.nextInt(ALLOWED_CHARS.length)]);
        }
        return sb.toString();
    }

    private String encode2Base64Url(byte[] data) {
        return new String(Base64.getUrlEncoder().withoutPadding().encode(data));
    }
}