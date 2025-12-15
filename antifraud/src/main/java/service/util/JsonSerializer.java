package service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static config.ApplicationConstant.AUDIT_SERIALIZATION_ERROR;


@Slf4j
@Component
@RequiredArgsConstructor
public class JsonSerializer {

    private final ObjectMapper objectMapper;

    public String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Ошибка сериализации: {}", e.getMessage());
            return AUDIT_SERIALIZATION_ERROR;
        }
    }
}
