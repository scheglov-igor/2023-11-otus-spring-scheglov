package ru.otus.hw.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
    private final List<Locale> possibleLocales;

    private final Map<String, String> fileNameByLocaleTag;

    public AppProperties(int rightAnswersCountToPass, List<String> possibleLocales, String locale, Map<String,
            String> fileNameByLocaleTag) {
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.possibleLocales = possibleLocales.stream().map(Locale::forLanguageTag).collect(Collectors.toList());
        this.locale =  Locale.forLanguageTag(locale);
        this.fileNameByLocaleTag = fileNameByLocaleTag;
    }

    @Override
    public Locale setLocale(String localeStr) {
        String localeTag = switch (localeStr.toLowerCase().trim()) {
            case "en", "en-us" -> "en-US";
            case "ru", "ru-ru" -> "ru-RU";
            default -> null;
        };
        if (localeTag != null) {
            this.locale = Locale.forLanguageTag(localeTag);
        }
        return locale;
    }

    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale.toLanguageTag());
    }

    @Override
    public List<String> getPossibleLanguages() {
        return possibleLocales.stream().map(Locale::getLanguage).collect(Collectors.toList());
    }
}
