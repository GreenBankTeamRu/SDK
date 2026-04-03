package ru.sberbank.sbbol.sberbusinessapi.model.paymentlink;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Ответ со статусом регистрации функциональной ссылки
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SbpB2BLinkCreateResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Информация об ошибке. Обязательно при наличии ошибки
     */
    @Size(max = 20, message = "Список ошибок не должен превышать 20 элементов")
    @JsonProperty("errors")
    private List<Object> errors;

    /**
     * Response entity
     */
    @NotNull(message = "Ответ обязателен")
    @Valid
    @JsonProperty("responseEntity")
    private ResponseEntity responseEntity;

    /**
     * Response entity
     */
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseEntity implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * Информация о статусе и данные ссылки
         */
        @NotNull(message = "Данные ссылки обязательны")
        @Valid
        @JsonProperty("linkData")
        private LinkDataResponse linkData;

        /**
         * ОГРН получателя денежных средств
         */
        @Size(max = 15, message = "ОГРН не должен превышать 15 символов")
        @JsonProperty("ogrn")
        private String ogrn;

        /**
         * Email для рассылки (зарезервировано, не используется)
         */
        @Size(max = 3, message = "Список email не должен превышать 3 элемента")
        @JsonProperty("email")
        private List<@Email(message = "Некорректный формат email") String> email;

        /**
         * Данные ссылки от НСПК
         */
        @NotNull(message = "Данные НСПК обязательны")
        @Valid
        @JsonProperty("linkDataSbp")
        private LinkDataSbp linkDataSbp;
    }

    /**
     * Информация о статусе и данные ссылки
     */
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LinkDataResponse implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * ID ссылки в ППРБ
         */
        @NotNull(message = "ID ссылки обязателен")
        @JsonProperty("paymentLinkId")
        private UUID paymentLinkId;

        /**
         * Тип ссылки
         */
        @NotNull(message = "Тип ссылки обязателен")
        @JsonProperty("linkType")
        private LinkType linkType;

        /**
         * Номер счета
         */
        @NotNull(message = "Номер счета обязателен")
        @Size(max = 20, message = "Номер счета не должен превышать 20 символов")
        @JsonProperty("account")
        private String account;

        /**
         * Сумма в счете на оплату в копейках
         */
        @Size(max = 12, message = "Сумма не должна превышать 12 символов")
        @JsonProperty("amount")
        private String amount;

        /**
         * Индикатор наличия НДС
         */
        @NotNull(message = "Индикатор НДС обязателен")
        @JsonProperty("takeTax")
        private Boolean takeTax;

        /**
         * Сумма НДС в копейках
         */
        @Size(max = 12, message = "Сумма НДС не должна превышать 12 символов")
        @JsonProperty("totalTaxAmount")
        private String totalTaxAmount;

        /**
         * Назначение платежа
         */
        @Size(max = 210, message = "Назначение платежа не должно превышать 210 символов")
        @JsonProperty("paymentPurpose")
        private String paymentPurpose;

        /**
         * Дата, до какого числа должна действовать многофункциональная ссылка, округленная до полного дня
         */
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
         * Статус функциональной ссылки
         */
        @NotNull(message = "Статус ссылки обязателен")
        @JsonProperty("status")
        private LinkStatus status;

        /**
         * Ссылка для автоматического возврата плательщика на сайт
         */
        @Size(max = 1024, message = "URL не должен превышать 1024 символа")
        @JsonProperty("redirectUrl")
        private String redirectUrl;

        /**
         * Наименование ТСП
         */
        @Size(max = 35, message = "Наименование ТСП не должно превышать 35 символов")
        @JsonProperty("brandName")
        private String brandName;
    }

    /**
     * Данные ссылки от НСПК
     */
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LinkDataSbp implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * Идентификатор функциональной ссылки
         */
        @NotNull(message = "Идентификатор ссылки НСПК обязателен")
        @Size(max = 254, message = "Идентификатор не должен превышать 254 символа")
        @JsonProperty("qrcId")
        private String qrcId;

        /**
         * Ссылка от СБП
         */
        @Size(max = 6000, message = "Ссылка не должна превышать 6000 символов")
        @JsonProperty("payload")
        private String payload;

        /**
         * QR-код (картинка)
         */
        @JsonProperty("image")
        private QrImage image;
    }

    /**
     * QR-код (изображение)
     */
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QrImage implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * Формат qr кода
         */
        @Size(max = 254, message = "MediaType не должен превышать 254 символа")
        @JsonProperty("mediaType")
        private String mediaType;

        /**
         * Закодированное изображение (base64)
         */
        @Size(max = 4000, message = "Изображение не должно превышать 4000 символов")
        @JsonProperty("content")
        private String content;
    }
}
