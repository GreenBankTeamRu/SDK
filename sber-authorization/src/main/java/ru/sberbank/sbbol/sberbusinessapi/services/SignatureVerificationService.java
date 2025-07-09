package ru.sberbank.sbbol.sberbusinessapi.services;

public interface SignatureVerificationService {

    /**
     * Проверка подписи
     * @param jwt данные для проверки
     * @return результат проверки подписи
     */
    boolean verifyJwt(String jwt);
}
