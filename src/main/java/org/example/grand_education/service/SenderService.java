package org.example.grand_education.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.grand_education.application.Messages;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Slf4j
@Service
@RequiredArgsConstructor
public class SenderService {

    public String message(String fieldName, String defaultText) {
        return getFieldValue(fieldName, defaultText);
    }

    private String getFieldValue(String fieldName, String defaultValue) {
        try {
            Field field = Messages.class.getDeclaredField(fieldName);
            return (String) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
