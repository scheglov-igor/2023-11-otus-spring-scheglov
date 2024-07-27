package ru.otus.hw.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


@ConfigurationProperties(prefix = "test")
// Использовать @ConfigurationProperties.
// Сейчас класс соответствует файлу настроек. Чтобы они сюда отобразились нужно только правильно разместить аннотации
public class AppProperties implements TestConfig, TestFileNameProvider, LocaleConfig {

    @Getter
    private final int rightAnswersCountToPass;

    @Getter
    private Locale locale;

    @Getter
    private final Map<String, Locale> possibleLocales;

    private final Map<String, String> fileNameByLocaleTag;

    public AppProperties(int rightAnswersCountToPass, String locale, Map<String, String> fileNameByLocaleTag) {

        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.possibleLocales = fileNameByLocaleTag.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Locale.forLanguageTag(entry.getKey()).getLanguage(),
                        entry -> Locale.forLanguageTag(entry.getKey())));

        this.locale =  Locale.forLanguageTag(locale);
        this.fileNameByLocaleTag = fileNameByLocaleTag;
    }

    @Override
    public Locale setLocale(String localeStr) {
        if (localeStr != null) {
            this.locale = possibleLocales.get(localeStr);
        }
        return locale;
    }

    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale.toLanguageTag());
    }

    @Override
    public List<String> getPossibleLanguages() {
        return new ArrayList<>(possibleLocales.keySet());
    }
}
