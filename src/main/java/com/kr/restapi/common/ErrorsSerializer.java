package com.kr.restapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;
import java.util.Objects;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray(); // Errors에는 error의 종류가 여러개이기 때문에 배열의 형태로 담아준다.

        // errors객체에 error를 담을 때 rejectValue로 담는 경우(EventValidator)
        errors.getFieldErrors().forEach(e -> {
            try {
                gen.writeStartObject(); // json Object

                    gen.writeStringField("field", e.getField());
                    gen.writeStringField("objectName", e.getObjectName());
                    gen.writeStringField("code", e.getCode());
                    gen.writeStringField("defaultMessage", e.getDefaultMessage());
                    Object rejectValue = e.getRejectedValue();
                    if(Objects.nonNull(rejectValue)) {
                        gen.writeStringField("rejectValue", rejectValue.toString());
                    } else {
                        gen.writeStringField("rejectValue", null);
                    }

                gen.writeEndObject();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        // errors객체에 error를 담을 때 reject로 담는 경우(EventValidator)
        errors.getGlobalErrors().forEach(e -> {
            try {
                gen.writeStartObject();

                    gen.writeStringField("objectName", e.getObjectName());
                    gen.writeStringField("code", e.getCode());
                    gen.writeStringField("defaultMessage", e.getDefaultMessage());

                gen.writeEndObject();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        gen.writeEndArray();
    }
}
