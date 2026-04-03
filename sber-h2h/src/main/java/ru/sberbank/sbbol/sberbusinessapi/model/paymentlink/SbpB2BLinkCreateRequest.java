package ru.sberbank.sbbol.sberbusinessapi.model.paymentlink;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Запрос на создание и регистрацию функциональной ссылки
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SbpB2BLinkCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Данные ссылки
     */
    @NotNull(message = "Данные ссылки обязательны")
    @Valid
    @JsonProperty("linkData")
    private LinkDataRequest linkData;

    /**
     * Email для рассылки (зарезервировано, не используется)
     */
    @Size(max = 3, message = "Список email не должен превышать 3 элемента")
    @JsonProperty("email")
    private List<@Email(message = "Некорректный формат email") String> email;

    /**
     * Данные ссылки для создания
     */
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LinkDataRequest implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * Тип ссылки (одноразовая или многоразовая)
         */
        @JsonProperty("linkType")
        private LinkType linkType;

        /**
         * Номер счета
         */
        @Size(max = 20, message = "Номер счета не должен превышать 20 символов")
        @Pattern(regexp = "^[0-9]{20}$", message = "Номер счета должен содержать ровно 20 цифр")
        @JsonProperty("account")
        private String account;

        /**
         * Сумма в счете на оплату в копейках.
         * Если тип ссылки oneTime (одноразовая), то поле обязательное.
         * Если тип ссылки reusable (многоразовая), то обязательно при takeTax==true
         */
        @Size(max = 12, message = "Сумма не должна превышать 12 символов")
        @Pattern(regexp = "^[0-9]{1,12}$", message = "Сумма должна содержать от 1 до 12 цифр")
        @JsonProperty("amount")
        private String amount;

        /**
         * Индикатор наличия НДС
         */
        @JsonProperty("takeTax")
        private Boolean takeTax;

        /**
         * Сумма НДС в копейках.
         * Обязателен при значении параметра linkType==oneTime и takeTax==true
         */
        @Size(max = 12, message = "Сумма НДС не должна превышать 12 символов")
        @Pattern(regexp = "^[0-9]{1,12}$", message = "Сумма НДС должна содержать от 1 до 12 цифр")
        @JsonProperty("totalTaxAmount")
        private String totalTaxAmount;

        /**
         * Назначение платежа. Если тип ссылки oneTime (одноразовая), то поле обязательное
         */
        @Size(max = 210, message = "Назначение платежа не должно превышать 210 символов")
        @JsonProperty("paymentPurpose")
        private String paymentPurpose;

        /**
         * Дата, до какого числа должна действовать ссылка, округленная до полного дня.
         * Одноразовая ссылка - 3 дня, многоразовая ссылка - 90 дней, округление до суток
         */
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$",
                message = "Дата должна быть в формате YYYY-MM-DD")
        @JsonProperty("dayLife")
        private String dayLife;

        /**
         * Название документа
         */
        @Size(max = 100, message = "Название не должно превышать 100 символов")
        @Pattern(regexp = "^[\\u0020-\\u007E\\u0410-\\u044F\\u2116]{0,100}$",
                message = "Название содержит недопустимые символы")
        @JsonProperty("linkName")
        private String linkName;

        /**
         * Ссылка для автоматического возврата плательщика на сайт
         */
        @Size(max = 1024, message = "URL не должен превышать 1024 символа")
        @Pattern(regexp = "^https?://.*", message = "URL должен начинаться с http:// или https://")
        @JsonProperty("redirectUrl")
        private String redirectUrl;

        /**
         * Наименование ТСП
         */
        @Size(max = 35, message = "Наименование ТСП не должно превышать 35 символов")
        @JsonProperty("brandName")
        private String brandName;

        /**
         * ИНН плательщика
         */
        @Size(max = 50, message = "ИНН не должен превышать 50 символов")
        @Pattern(regexp = "^[0-9]{10,12}$", message = "ИНН должен содержать 10 или 12 цифр")
        @JsonProperty("payerInn")
        private String payerInn;
    }
}