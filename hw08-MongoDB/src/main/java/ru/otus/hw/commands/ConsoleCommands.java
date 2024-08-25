package ru.otus.hw.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import org.h2.tools.Console;

import java.sql.SQLException;

@ShellComponent
public class ConsoleCommands {

    @ShellMethod
    public void console() throws SQLException {
        Console.main();
    }
}