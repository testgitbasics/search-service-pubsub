package com.searchplatform.searchservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/translations")
public class TranslationController {

    @GetMapping("/{locale}")
    public Map<String, String> getTranslations(@PathVariable String locale) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        InputStream inputStream;

        if ("fr".equals(locale)) {
            inputStream = getClass().getResourceAsStream("/i18n/fr.json");
        } else {
            inputStream = getClass().getResourceAsStream("/i18n/en.json");
        }

        return mapper.readValue(inputStream, new TypeReference<>() {});
    }
}
