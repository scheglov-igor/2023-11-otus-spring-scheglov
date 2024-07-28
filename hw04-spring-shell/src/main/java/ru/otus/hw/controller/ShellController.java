package ru.otus.hw.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.LocalizedMessagesService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestRunnerService;


import java.util.Locale;


@ShellComponent
public class ShellController extends AbstractShellComponent {
    private final LocaleConfig localeConfig;

    private final LocalizedMessagesService localizedMessagesService;

    private final StudentService studentService;

    private final LoginContext loginContext;

    private final TestRunnerService testRunnerService;

    public ShellController(LocaleConfig localeConfig,
                       @Qualifier("localizedMessagesServiceImpl") LocalizedMessagesService localizedMessagesService,
                       TestRunnerService testRunnerService, StudentService studentService, LoginContext loginContext) {
        this.localeConfig = localeConfig;
        this.localizedMessagesService = localizedMessagesService;
        this.testRunnerService = testRunnerService;
        this.studentService = studentService;
        this.loginContext = loginContext;
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
        var student = studentService.determineCurrentStudent();
        loginContext.login(student);
        return localizedMessagesService.getMessage("helloUser", student.firstName(), student.lastName());
    }

    @ShellMethod(value = "Start testing", key = {"t", "test", "start"})
    @ShellMethodAvailability(value = "isTestingAvailable")
    public String test () {
        var testResult = testRunnerService.executeTestFor(loginContext.getCurrentStudent());
        testRunnerService.showResult(testResult);
        return localizedMessagesService.getMessage("testFinished");
    }

    private Availability isTestingAvailable() {
        return loginContext.isUserLoggedIn() ?
                Availability.available() :
                Availability.unavailable(localizedMessagesService.getMessage("youShouldLogin"));
    }

}