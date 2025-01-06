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
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import javax.sql.DataSource;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class BookMigration {

    private final Logger logger = LoggerFactory.getLogger("BookMigration");

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final DataSource dataSource;

    private final BookRepository bookRepository;

    private final AppProps appProps;


    @Bean
    public Step bookMigrationStep(ItemReader<Book> bookReader,
                                    ItemProcessor<Book, Book> bookProcessor,
                                    ItemWriter<Book> compositeBookWriter) {
        return new StepBuilder("bookMigrationStep", jobRepository)
                .<Book, Book>chunk(appProps.getChunksize(), platformTransactionManager)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(compositeBookWriter)
                .build();
    }


    @Bean
    public RepositoryItemReader<Book> bookReader() {
        return new RepositoryItemReaderBuilder<Book>()
                .name("bookReader")
                .repository(bookRepository)
                .methodName("findAll")
                .pageSize(appProps.getChunksize())
                .sorts(new HashMap<>())
                .build();
    }


    @Bean
    public ItemProcessor<Book, Book> bookProcessor() {
        return item -> item;
    }


    @Bean
    public CompositeItemWriter<Book> compositeBookWriter(
            JdbcBatchItemWriter<Book> bookIdsWriter,
            JdbcBatchItemWriter<Book> bookWriter) {

        return new CompositeItemWriterBuilder<Book>()
                .delegates(bookIdsWriter, bookWriter)
                .build();
    }


    @Bean
    public JdbcBatchItemWriter<Book> bookIdsWriter() {
        return new JdbcBatchItemWriterBuilder<Book>()
                .dataSource(dataSource)
                .sql("insert into books_ids (mongo_id) values (:id)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }



    @Bean
    public JdbcBatchItemWriter<Book> bookWriter() {
        return new JdbcBatchItemWriterBuilder<Book>()
                .dataSource(dataSource)
                .sql("""
                    insert into books (id, title, author_id)
                    values (
                    (select sql_id from books_ids where mongo_id = :id), 
                    :title, 
                    (select sql_id from authors_ids where mongo_id = :author.id))
                 """)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }


    @Bean
    public TaskletStep bookCreateTmpStep() {
        return new StepBuilder("bookCreateTmpStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(this.dataSource)
                            .execute("create table IF NOT EXISTS books_ids (sql_id bigserial, mongo_id varchar)");
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }



    @Bean
    public TaskletStep bookDeleteTmpStep() {
        return new StepBuilder("bookDeleteTmpStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(this.dataSource)
                            .execute("drop table books_ids");
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }


}
