package io.synthesized.jdbcdemo.service.dao.tdk;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.synthesized.tdktc.SynthesizedTDK;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@TestConfiguration
@ComponentScan({"io.synthesized.jdbcdemo.service"})
public class TestConfig {
    private final Network network;
    private final PostgreSQLContainer<?> output;

    public TestConfig() throws URISyntaxException, IOException {
        final URL config = Thread.currentThread().getContextClassLoader().getResource("config.yaml");
        String configString = Files.readString(Paths.get(config.toURI()));
        network = Network.newNetwork();
        try (PostgreSQLContainer<?> input = getContainer("input", true, -1)) {
            output = getContainer("output", false, 6011);
            Startables.deepStart(input, output).join();
            System.out.printf(">>> Output container: %s, login: %s, password: %s%n",
                    output.getJdbcUrl(), output.getUsername(), output.getPassword());
            new SynthesizedTDK().transform(input, output, configString);
        }
    }


    PostgreSQLContainer<?> getContainer(String name, boolean init, int port) {
        String scriptPath = "io/synthesized/jdbcdemo/dbcreate.sql";
        var result = new PostgreSQLContainer<>("postgres:11.1")
                .withDatabaseName(name)
                .withUsername("user")
                .withPassword("password")
                .withNetwork(network);
        if (port > 0) {
            //This is needed for stable port allocation for simpler demo purposes only.
            //Normally, one should not use static port allocation for Testcontainers
            result = result.withExposedPorts(PostgreSQLContainer.POSTGRESQL_PORT)
                    .withCreateContainerCmdModifier(cmd -> cmd.getHostConfig().withPortBindings(
                            new PortBinding(Ports.Binding.bindPort(port),
                                    new ExposedPort(PostgreSQLContainer.POSTGRESQL_PORT))));
        }
        result = init ? result.withInitScript(scriptPath) : result;

        return result;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(output.getJdbcUrl());
        config.setUsername(output.getUsername());
        config.setPassword(output.getPassword());
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @PreDestroy
    void tearDown() {
        output.close();
    }
}
