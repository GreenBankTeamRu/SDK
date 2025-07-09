package ru.sberbank.sbbol.sberbusinessapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentState {
    private String bankComment;
    private String bankStatus;
    private String crucialFieldsHash;
    private String channelInfo;
}
