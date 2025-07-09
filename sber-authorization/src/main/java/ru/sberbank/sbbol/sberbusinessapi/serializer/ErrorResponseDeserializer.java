package ru.sberbank.sbbol.sberbusinessapi.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.sberbank.sbbol.sberbusinessapi.model.ErrorResponse;

import java.io.IOException;

public class ErrorResponseDeserializer extends StdDeserializer<ErrorResponse> {

    public ErrorResponseDeserializer() {
        super(ErrorResponse.class);
    }

    @Override
    public ErrorResponse deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String error = getFieldValue(node, "error", "errorCode", "cause");
        String description = getFieldValue(node, "error_description", "errorMsg", "message");

        return new ErrorResponse(error, description);
    }

    private String getFieldValue(JsonNode node, String... fieldNames) {
        for (String fieldName : fieldNames) {
            JsonNode fieldNode = node.get(fieldName);
            if (fieldNode != null) {
                return fieldNode.asText();
            }
        }
        return null;
    }
}