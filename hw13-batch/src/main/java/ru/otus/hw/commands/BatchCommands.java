package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Properties;

import static ru.otus.hw.config.JobConfig.DB_MIGRATION_JOB_NAME;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private final JobOperator jobOperator;

    private final JobExplorer jobExplorer;

    @ShellMethod(value = "startMigrationJobWithJobOperator", key = "sm")
    public void startMigrationJobWithJobOperator() throws Exception {
        Properties properties = new Properties();

        Long executionId = jobOperator.start(DB_MIGRATION_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "showInfo", key = "i")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(DB_MIGRATION_JOB_NAME));
    }
}
