package ru.sberbank.sbbol.sberbusinessapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private Map<String, Object> userInfoBodyResponse;
    private String jwt;
}
