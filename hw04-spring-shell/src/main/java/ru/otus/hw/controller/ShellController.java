package ru.otus.hw.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.Availability;

import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.LocalizedMessagesService;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

import java.util.Locale;


@ShellComponent
public class ShellController extends AbstractShellComponent {
    private final LocaleConfig localeConfig;

    private final LocalizedMessagesService localizedMessagesService;

    private final StudentService studentService;

    private final TestService testService;

    private final ResultService resultService;

    //TODO куда бы это вынести, чтобы оставить приложение statless???
    // или не нужно разделять логин и само тестирование? Тогда spring shell будет совсем мало...
    private Student student;

    public ShellController(LocaleConfig localeConfig,
                           @Qualifier("localizedMessagesServiceImpl") LocalizedMessagesService localizedMessagesService,
                           StudentService studentService, TestService testService, ResultService resultService) {
        this.localeConfig = localeConfig;
        this.localizedMessagesService = localizedMessagesService;
        this.studentService = studentService;
        this.testService = testService;
        this.resultService = resultService;
    }

    @ShellMethod(value = "Select language", key = {"language", "locale"})
    public String language (
            @ShellOption(defaultValue = ShellOption.NULL) String localeStr) {

        if (localeStr == null) {
            return localizedMessagesService.getMessage("possibleLanguages", localeConfig.getPossibleLanguages());
        } else {
            Locale locale = localeConfig.setLocale(localeStr);
            return localizedMessagesService.getMessage("selectedLanguage", locale.getLanguage());
        }
    }

    @ShellMethod(value = "login user", key = {"l", "login"})
    public String login() {
        student = studentService.determineCurrentStudent();
        return localizedMessagesService.getMessage("helloUser", student.firstName(), student.lastName());
    }

    @ShellMethod(value = "Start testing", key = {"t", "test", "start"})
    @ShellMethodAvailability(value = "isTestingAvailable")
    public String test () {
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
        return localizedMessagesService.getMessage("testFinished");
    }

    private Availability isTestingAvailable() {
        return student == null ?
                Availability.unavailable(localizedMessagesService.getMessage("youShouldLogin")) :
                Availability.available();
    }

}