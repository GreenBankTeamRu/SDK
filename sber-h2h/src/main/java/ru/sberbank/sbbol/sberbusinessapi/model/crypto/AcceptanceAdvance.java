package ru.sberbank.sbbol.sberbusinessapi.model.crypto;

import lombok.Data;

@Data
public class AcceptanceAdvance {
    private String bankComment;
    private String bankStatus;
    private String channelInfo;
}
