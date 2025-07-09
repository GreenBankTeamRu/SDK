package ru.sberbank.sbbol.sberbusinessapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private TokenBodyResponse tokenBodyResponse;
    private String signature;

    /**
     * Расшифрованный idToken(Хедер часть) в случае передачи need_user_info = true в
     */
    private String header;


    /**
     * Расшифрованный idToken(body часть) в случае передачи need_user_info = true в
     */
    private String body;

    private String jwt;
}
