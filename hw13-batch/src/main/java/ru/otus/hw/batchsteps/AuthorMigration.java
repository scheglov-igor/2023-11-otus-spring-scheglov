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
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import javax.sql.DataSource;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class AuthorMigration {

    private final Logger logger = LoggerFactory.getLogger("AuthorMigration");

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final DataSource dataSource;

    private final AuthorRepository authorRepository;

    private final AppProps appProps;


    @Bean
    public Step authorMigrationStep(ItemReader<Author> authorReader,
                                    ItemProcessor<Author, Author> authorProcessor,
                                    ItemWriter<Author> compositeAuthorWriter) {
        return new StepBuilder("authorMigrationStep", jobRepository)
                .<Author, Author>chunk(appProps.getChunksize(), platformTransactionManager)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(compositeAuthorWriter)
                .build();
    }


    @Bean
    public RepositoryItemReader<Author> authorReader() {
        return new RepositoryItemReaderBuilder<Author>()
                .name("authorReader")
                .repository(authorRepository)
                .methodName("findAll")
                .pageSize(appProps.getChunksize())
                .sorts(new HashMap<>())
                .build();
    }


    @Bean
    public ItemProcessor<Author, Author> authorProcessor() {
        return item -> item;
    }


    @Bean
    public CompositeItemWriter<Author> compositeAuthorWriter(
            JdbcBatchItemWriter<Author> authorIdsWriter,
            JdbcBatchItemWriter<Author> authorWriter) {

        return new CompositeItemWriterBuilder<Author>()
                .delegates(authorIdsWriter, authorWriter)
                .build();
    }



    @Bean
    public JdbcBatchItemWriter<Author> authorIdsWriter() {
        return new JdbcBatchItemWriterBuilder<Author>()
                .dataSource(dataSource)
                .sql("insert into authors_ids (mongo_id) values (:id)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }




    @Bean
    public JdbcBatchItemWriter<Author> authorWriter() {
        return new JdbcBatchItemWriterBuilder<Author>()
                .dataSource(dataSource)
                .sql("insert into authors (id, full_name) " +
                        "values ((select sql_id from authors_ids where mongo_id = :id), :fullName)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }



    @Bean
    public TaskletStep authorCreateTmpStep() {
        return new StepBuilder("authorCreateTmpStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(this.dataSource)
                            .execute("create table IF NOT EXISTS  authors_ids (sql_id bigserial, mongo_id varchar)");
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }



    @Bean
    public TaskletStep authorDeleteTmpStep() {
        return new StepBuilder("authorDeleteTmpStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(this.dataSource)
                            .execute("drop table authors_ids");
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }


}
