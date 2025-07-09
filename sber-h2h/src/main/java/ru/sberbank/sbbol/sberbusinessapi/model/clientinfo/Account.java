package ru.sberbank.sbbol.sberbusinessapi.model.clientinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String bic; // БИК банка
    private List<AccountBlockInfo> blockedQueuesInfo; // Блокировки по очередности
    private List<AccountBlockInfo> blockedSumQueuesInfo; // Блокировки по очередности на сумму
    private List<AccountBlockInfo> blockedSums; // Заблокированные суммы
    private Boolean business; // Признак бизнес-счета
    private Integer cdiAcptDocQnt; // Документы, ожидающие акцепта (количество)
    private Double cdiAcptDocSum; // Документы, ожидающие акцепта (сумма)
    private Integer cdiCart2DocQnt; // Документы в картотеке 2 (количество)
    private Double cdiCart2DocSum; // Документы в картотеке 2 (сумма)
    private Integer cdiPermDocQnt; // Документы, ожидающие разрешения (количество)
    private Double cdiPermDocSum; // Документы, ожидающие разрешения (сумма)
    private String closeDate; // Дата закрытия счета
    private String comment; // Примечание
    private Boolean creditBlocked; // Блокировка по кредиту
    private String creditBlockedBeginDate; // Дата начала блокировки по кредиту
    private String creditBlockedCause; // Основание блокировки по кредиту
    private String creditBlockedEndDate; // Дата снятия блокировки по кредиту
    private String currencyCode; // Код валюты счета
    private Boolean dbo; // Признак обслуживания в ДБО
    private Boolean debitBlocked; // Блокировка по дебету
    private String debitBlockedBeginDate; // Дата начала блокировки по дебету
    private String debitBlockedCause; // Основание блокировки по дебету
    private String debitBlockedEndDate; // Дата снятия блокировки по дебету
    private Double minBalance; // Минимальный остаток
    private String name; // Наименование счета
    private Boolean notDelay; // Признак неотложных платежей
    private String number; // Номер счета
    private String openDate; // Дата открытия счета
    private String state; // Состояние счета
    private String type; // Тип счета
    private Boolean urgent; // Признак срочных платежей
}
