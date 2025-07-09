package ru.sberbank.sbbol.sberbusinessapi.model;

import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LinkedDoc {

    /**
     * Идентификатор документа (заявления на страхование экспортного контракта) во внешней системе (UUID),
     * к которому необходимо привязать платежное поручение
     */
    private UUID docExtId;

    private String type = "ExportContractInsure";

}
