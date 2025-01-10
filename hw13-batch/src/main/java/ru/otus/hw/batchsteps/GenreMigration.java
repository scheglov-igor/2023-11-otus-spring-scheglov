package ru.otus.hw.batchsteps;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.config.AppProps;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import javax.sql.DataSource;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class GenreMigration {

    private final Logger logger = LoggerFactory.getLogger("GenreMigration");

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final DataSource dataSource;

    private final GenreRepository genreRepository;

    private final AppProps appProps;


    @Bean
    public Step genreMigrationStep(ItemReader<Genre> genreReader,
                                   ItemProcessor<Genre, Genre> genreProcessor,
                                   ItemWriter<Genre> compositeGenreWriter) {
        return new StepBuilder("transformPersonsStep", jobRepository)
                .<Genre, Genre>chunk(appProps.getChunksize(), platformTransactionManager)
                .reader(genreReader)
                .processor(genreProcessor)
                .writer(compositeGenreWriter)
                .build();
    }


    @Bean
    public RepositoryItemReader<Genre> genreReader() {
        return new RepositoryItemReaderBuilder<Genre>()
                .name("genreReader")
                .repository(genreRepository)
                .methodName("findAll")
                .pageSize(appProps.getChunksize())
                .sorts(new HashMap<>())
                .build();
    }


    @Bean
    public ItemProcessor<Genre, Genre> genreProcessor() {
        return item -> item;
    }


    @Bean
    public CompositeItemWriter<Genre> compositeGenreWriter(
            JdbcBatchItemWriter<Genre> genreIdsWriter,
            JdbcBatchItemWriter<Genre> genreWriter) {

        return new CompositeItemWriterBuilder<Genre>()
                .delegates(genreIdsWriter, genreWriter)
                .build();
    }


    @Bean
    public JdbcBatchItemWriter<Genre> genreIdsWriter() {
        return new JdbcBatchItemWriterBuilder<Genre>()
                .dataSource(dataSource)
                .sql("insert into genres_ids (mongo_id) values (:id)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }


    @Bean
    public JdbcBatchItemWriter<Genre> genreWriter() {
        return new JdbcBatchItemWriterBuilder<Genre>()
                .dataSource(dataSource)
                .sql("insert into genres (id, name) " +
                        "values ((select sql_id from genres_ids where mongo_id = :id), :name)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }



    @Bean
    public TaskletStep genreCreateTmpStep() {
        return new StepBuilder("genreCreateTmpStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(this.dataSource)
                            .execute("create table IF NOT EXISTS  genres_ids (sql_id bigserial, mongo_id varchar)");
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }




    @Bean
    public TaskletStep genreDeleteTmpStep() {
        return new StepBuilder("genreDeleteTmpStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(this.dataSource).execute("drop table genres_ids");
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }


}
