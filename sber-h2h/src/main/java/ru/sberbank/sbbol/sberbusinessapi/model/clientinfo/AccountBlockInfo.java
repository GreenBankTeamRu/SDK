package ru.sberbank.sbbol.sberbusinessapi.model.clientinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBlockInfo {
    /**
     * Дата начала ограничения
     */
    private String beginDate;
    /**
     *  Блокировка по очередности
     */
    private Integer blockedQueues;
    /**
     * Основание ареста
     */
    private String cause;
    /**
     *  Дата снятия ограничения
     */
    private String endDate;
    /**
     * Орган, наложивший арест
     */
    private String initiator;
    /**
     * Заблокированная сумма
     */
    private Double sum;
    /**
     * Код налогового органа
     */
    private String taxAuthorityCode;
}