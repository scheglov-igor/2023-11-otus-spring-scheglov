package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@RequiredArgsConstructor
@Configuration
public class JobConfig {

    public static final String DB_MIGRATION_JOB_NAME = "dbMigrationJob";

    private final Logger logger = LoggerFactory.getLogger("JobConfig");

    private final JobRepository jobRepository;

    private final Step authorCreateTmpStep;

    private final Step authorMigrationStep;

    private final Step authorDeleteTmpStep;

    private final Step genreCreateTmpStep;

    private final Step genreMigrationStep;

    private final Step genreDeleteTmpStep;

    private final Step bookCreateTmpStep;

    private final Step bookMigrationStep;

    private final Step bookDeleteTmpStep;

    private final Step bookGenreMigrationStep;

    private final Step commentMigrationStep;

    @Bean
    public Job dbMigrationJob(
                              ) {

        return new JobBuilder(DB_MIGRATION_JOB_NAME, jobRepository)

                .start(authorCreateTmpStep)
                .next(authorMigrationStep)

                .next(genreCreateTmpStep)
                .next(genreMigrationStep)

                .next(bookCreateTmpStep)
                .next(bookMigrationStep)

                .next(bookGenreMigrationStep)
                .next(commentMigrationStep)

                .next(authorDeleteTmpStep)
                .next(genreDeleteTmpStep)
                .next(bookDeleteTmpStep)

                .build();
    }

}
