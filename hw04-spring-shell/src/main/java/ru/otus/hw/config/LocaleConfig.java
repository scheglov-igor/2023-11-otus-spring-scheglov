package ru.otus.hw.config;

import java.util.List;
import java.util.Locale;

public interface LocaleConfig {
    Locale getLocale();

    Locale setLocale(String locale);

    List<String> getPossibleLanguages();

}
