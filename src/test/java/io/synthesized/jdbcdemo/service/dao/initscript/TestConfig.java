package io.synthesized.jdbcdemo.service.dao.initscript;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@TestConfiguration
@ComponentScan({"io.synthesized.jdbcdemo.service"})
public class TestConfig {
    private final PostgreSQLContainer<?> db;

    public TestConfig() {
        String init = "init.sql";
        db = new PostgreSQLContainer<>("postgres:11.1")
                .withDatabaseName("test")
                .withUsername("user")
                .withPassword("password")
                .withInitScript(init);
        db.start();
    }


    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(db.getJdbcUrl());
        config.setUsername(db.getUsername());
        config.setPassword(db.getPassword());
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @PreDestroy
    void tearDown() {
        db.close();
    }
}
