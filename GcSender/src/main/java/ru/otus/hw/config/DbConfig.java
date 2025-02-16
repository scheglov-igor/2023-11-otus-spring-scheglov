package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class DbConfig {

    private final Config config;

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.driverClassName(getDriverClassName());
        dataSourceBuilder.url(config.getJdbcUrl());
        dataSourceBuilder.username(config.getDbUser());
        dataSourceBuilder.password(config.getDbPassword());
        return dataSourceBuilder.build();
    }

    private String getDriverClassName() {
        return "org.postgresql.Driver";
    }

}
