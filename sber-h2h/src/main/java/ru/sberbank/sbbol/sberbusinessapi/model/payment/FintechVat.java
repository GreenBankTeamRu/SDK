package ru.sberbank.sbbol.sberbusinessapi.model.payment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FintechVat {
    @JsonProperty("type")
    @NotNull
    private TypeEnum type;

    @JsonProperty("rate")
    @Pattern(regexp = "^[0-9]{0,2}$")
    private String rate;

    @JsonProperty("amount")
    private BigDecimal amount;

    public enum TypeEnum {
        INCLUDED("INCLUDED"),

        ONTOP("ONTOP"),

        NO_VAT("NO_VAT"),

        MANUAL("MANUAL");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static TypeEnum fromValue(String value) {
            for (TypeEnum b : TypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            return null;
        }
    }
}
