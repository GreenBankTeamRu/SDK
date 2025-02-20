package ru.sberbank.pprb.sbbol.draft;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.dcbqa.allureee.annotations.layers.ConfigurationTestLayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@JsonTest
@ActiveProfiles("test")
@DirtiesContext
@ContextConfiguration(initializers = {HibernatePluginCleanerInitializer.class})
@ConfigurationTestLayer
class UnknownJsonPropertiesTest {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Тест для проверки конфигурации ObjectMapper'а в контексте Spring.
     * <p>
     * Суть проверки сводится к тому, что ObjectMapper не должен выдавать ошибку в случае, если в JSON встречается неизвестное поле. Т.к.
     * добавление поля в модель является обратно совместимым изменением, наши сервисы должны быть толерантны к подобным изменениям.
     * <p>
     * В случае, если данный тест у вас падает, проверьте конфигурацию Jackson, удостоверьтесь, что свойство {@link
     * com.fasterxml.jackson.databind.DeserializationFeature#FAIL_ON_UNKNOWN_PROPERTIES} отключено.
     * <p>
     * См <a href="https://www.baeldung.com/spring-boot-customize-jackson-objectmapper">How to customize Jackson</a>.
     */
    @Test
    @DisplayName("Тест для проверки конфигурации ObjectMapper'а в контексте Spring")
    void testUnknownProperty() throws JsonProcessingException {
        var jsonContent = """
              {
                  "field": "expected",
                  "unknownField": "unexpected"
              }
            """;
        MyClass value = objectMapper.readValue(jsonContent, MyClass.class);
        assertNotNull(value);
        assertEquals("expected", value.getField());
    }


    private static class MyClass {
        private String field;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }

}
