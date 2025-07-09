package ru.sberbank.sbbol.sberbusinessapi.model.clientinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgKindActivity {
    private Integer code; // Код вида деятельности
    private String name; // Наименование вида деятельности
}