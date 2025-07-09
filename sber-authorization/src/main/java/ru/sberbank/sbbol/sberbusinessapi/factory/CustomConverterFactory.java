package ru.sberbank.sbbol.sberbusinessapi.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype. jsr310.JavaTimeModule;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class CustomConverterFactory extends Converter.Factory {

    private final JacksonConverterFactory jacksonConverterFactory;
    private final ScalarsConverterFactory scalarsConverterFactory;

    public CustomConverterFactory() {
        this.jacksonConverterFactory = CustomJacksonConverterFactory.create();
        this.scalarsConverterFactory = ScalarsConverterFactory.create();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type,
            Annotation[] parameterAnnotations,
            Annotation[] methodAnnotations,
            Retrofit retrofit
    ) {
        return jacksonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type,
            Annotation[] annotations,
            Retrofit retrofit
    ) {
        if (type.equals(String.class)) {
            // Для строковых данных используем ScalarsConverterFactory
            return (Converter<ResponseBody, String>) value -> {
                try {
                    Converter<ResponseBody, String> stringConverter =
                            (Converter<ResponseBody, String>) scalarsConverterFactory.responseBodyConverter(type, annotations, retrofit);
                    return stringConverter.convert(value);
                } finally {
                    value.close();
                }
            };
        } else if (type.equals(byte[].class)) {
            // Для бинарных данных возвращаем массив байт
            return (Converter<ResponseBody, byte[]>) value -> {
                try {
                    return value.bytes();
                } finally {
                    value.close();
                }
            };
        } else {
            // Для остальных типов используем JacksonConverterFactory
            return jacksonConverterFactory.responseBodyConverter(type, annotations, retrofit);
        }
    }
}