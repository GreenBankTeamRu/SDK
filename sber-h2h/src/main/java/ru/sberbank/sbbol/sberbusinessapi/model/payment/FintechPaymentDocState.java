package ru.sberbank.sbbol.sberbusinessapi.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FintechPaymentDocState {
    @JsonProperty("bankStatus")
    private String bankStatus;

    @JsonProperty("bankComment")
    private String bankComment;

    @JsonProperty("channelInfo")
    private String channelInfo;

    @JsonProperty("crucialFieldsHash")
    private String crucialFieldsHash;
}
