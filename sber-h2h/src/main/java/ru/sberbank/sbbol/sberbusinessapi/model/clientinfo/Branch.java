package ru.sberbank.sbbol.sberbusinessapi.model.clientinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Branch {
    private String address; // Адрес подразделения
    private String code; // Код подразделения
    private String name; // Наименование подразделения
}
