package ru.sberbank.sbbol.sberbusinessapi.services;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClientSecretGenerator {
    // Допустимые символы согласно RFC 4648 (A-Z, a-z, 0-9, _, -)
    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-";
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final int DEFAULT_LENGTH = 40;

    public static String generateClientSecret() {
        return generateClientSecret(DEFAULT_LENGTH);
    }

    public static String generateClientSecret(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Длина client_secret должна быть положительным числом");
        }

        return RANDOM.ints(length, 0, ALLOWED_CHARACTERS.length())
                .mapToObj(ALLOWED_CHARACTERS::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}