package com.biao.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomLocalDateTimeDeserializer.class);

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String string = null;
        try {
            string = parser.getText().trim();
            if (StringUtils.isBlank(string)) {
                return null;
            }
            Long longStr = Long.parseLong(string);
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(longStr), ZoneOffset.of("+8"));
        } catch (Exception e) {
            LOGGER.error("时间解码错误：{}", string);
        }
        return null;
    }
}
