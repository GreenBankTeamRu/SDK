package ru.sberbank.sbbol.sberbusinessapi.services;

public interface SecurityService {

    //todo Сейчас на финтехе баг - регулярка ограничивает длину, поэтому будем принудительно генерить с длиной 43.
//    /**
//     * Генерирует код верификатора PKCE заданной длины.
//     *
//     * @param codeLength Длина кода верификатора. Должна находиться в диапазоне от 43 до 128 символов.
//     * @return Строку, представляющую собой код верификатора PKCE в формате base64url.
//     * @throws IllegalArgumentException Если длина кода выходит за допустимые пределы.
//     * @see <a href="https://datatracker.ietf.org/doc/html/rfc7636#page-8">RFC 7636</a>
//     */
//    String generatePKCECodeVerifier(int codeLength);

    /**
     * Генерирует случайный код верификатора PKCE стандартной длины (64 символа).
     *
     * @return Строку, представляющую собой случайно сгенерированный код верификатора PKCE в формате base64url.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc7636#page-8">RFC 7636</a>
     */
    String generatePKCECodeVerifier();

    /**
     * Генерирует код вызова PKCE на основе кода верификатора заданной длины.
     *
     * @param pKCECodeVerifier Код верификатора, используемый для генерации кода вызова.
     * @return Строку, представляющую собой код вызова PKCE в формате base64url.
     * @throws IllegalArgumentException Если длина кода выходит за допустимые пределы.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc7636#page-8">RFC 7636</a>
     */
    String generatePKCECodeChallenge(String pKCECodeVerifier);
}
