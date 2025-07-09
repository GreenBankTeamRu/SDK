package ru.sberbank.sbbol.sberbusinessapi.model.payroll;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FintechPayrollState {
    @JsonProperty("receiptStatus")
    private String receiptStatus;

    @JsonProperty("bankComment")
    private String bankComment;

    @JsonProperty("bankStatus")
    private String bankStatus;
}
