package ru.sberbank.sbbol.sberbusinessapi.model.clientinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DboContract {
    private String date; // Дата заключения договора
    private String number; // Номер договора
}
