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
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import javax.sql.DataSource;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class CommentMigration {

    private final Logger logger = LoggerFactory.getLogger("CommentMigration");

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final DataSource dataSource;

    private final CommentRepository commentRepository;

    private final AppProps appProps;


    @Bean
    public Step commentMigrationStep(ItemReader<Comment> commentReader,
                                    ItemWriter<Comment> commentWriter,
                                    ItemProcessor<Comment, Comment> commentProcessor) {
        return new StepBuilder("commentMigrationStep", jobRepository)
                .<Comment, Comment>chunk(appProps.getChunksize(), platformTransactionManager)
                .reader(commentReader)
                .processor(commentProcessor)
                .writer(commentWriter)
                .build();
    }


    @Bean
    public RepositoryItemReader<Comment> commentReader() {
        return new RepositoryItemReaderBuilder<Comment>()
                .name("commentReader")
                .repository(commentRepository)
                .methodName("findAll")
                .pageSize(appProps.getChunksize())
                .sorts(new HashMap<>())
                .build();
    }


    @Bean
    public ItemProcessor<Comment, Comment> commentProcessor() {
        return item -> item;
    }


    @Bean
    public JdbcBatchItemWriter<Comment> commentWriter() {
        return new JdbcBatchItemWriterBuilder<Comment>()
                .dataSource(dataSource)
                .sql("insert into comments (book_id, comment_text) " +
                        "values ((select sql_id from books_ids where mongo_id = :book.id), :commentText)")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }

}
