package ru.sberbank.sbbol.sberbusinessapi.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CustomJacksonConverterFactory {
    public static JacksonConverterFactory create() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return JacksonConverterFactory.create(objectMapper);
    }
}
