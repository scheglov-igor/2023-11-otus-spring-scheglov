package ru.otus.hw.batchsteps;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.config.AppProps;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import javax.sql.DataSource;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class BookGenreMigration {

    private final Logger logger = LoggerFactory.getLogger("BookGenreMigration");

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final DataSource dataSource;

    private final BookRepository bookRepository;

    private final AppProps appProps;


    @Bean
    public Step bookGenreMigrationStep(ItemReader<Book> bookGenreReader,
                                    ItemProcessor<Book, Book> bookGenreProcessor,
                                    ItemWriter<Book> bookGenreWriter) {
        return new StepBuilder("bookGenreMigrationStep", jobRepository)
                .<Book, Book>chunk(appProps.getChunksize(), platformTransactionManager)
                .reader(bookGenreReader)
                .processor(bookGenreProcessor)
                .writer(bookGenreWriter)

                .build();
    }


    @Bean
    public RepositoryItemReader<Book> bookGenreReader() {
        return new RepositoryItemReaderBuilder<Book>()
                .name("bookGenreReader")
                .repository(bookRepository)
                .methodName("findAll")
                .pageSize(appProps.getChunksize())
                .sorts(new HashMap<>())
                .build();
    }


    @Bean
    public ItemProcessor<Book, Book> bookGenreProcessor() {
        return item -> item;
    }



    @Bean
    public JdbcBatchItemWriter<Book> bookGenreWriter() {
        return new JdbcBatchItemWriterBuilder<Book>()
                .dataSource(dataSource)
                .sql("""
                    insert into books_genres (book_id, genre_id) 
                    select b.sql_id, g.sql_id
                    from books_ids b
                    join genres_ids g
                    where b.mongo_id = :id
                    and g.mongo_id in (:genreIds)
                    """)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }



}
