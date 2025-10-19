package io.github.jdlopez.admintool.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import io.github.jdlopez.admintool.domain.AdminSource;
import io.github.jdlopez.admintool.domain.ConfigInstance;
import io.github.jdlopez.admintool.domain.DbDriverType;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.annotations.CommandLineArguments;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ConfigurationService {

    @Inject
    @CommandLineArguments
    private String[] args;

    private ConfigInstance config;
    private Map<String, DbDriverType> drivers;
    private String fileName;

    private final ObjectMapper mapper;

    public ConfigurationService() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        fileName = "admintool.json";
    }

    @Startup
    void init() throws IOException {
        if (args.length > 1 && args[0].startsWith("--config")) {
            for (int i = 1; i < args.length; i++) {
                if (args[i].equals("--config") && args.length > i+1) {
                    // --config <filename>
                    fileName = args[1+1];
                }
            }
        }
        File f = new File(fileName);
        if (f.exists()) {
            config = mapper.readValue(f, ConfigInstance.class);
        } else {
            config = new ConfigInstance();
        }
        // drivers
        drivers = new HashMap<>();
        drivers.put("mariadb",
                new DbDriverType("mariadb", null,
                        "jdbc:mariadb://{host}:{port}/{database}"));
    }

    public ConfigInstance getConfigInstances() {
        return config;
    }

    public Collection<DbDriverType> getDrivers() {
        return drivers.values();
    }

    public DbDriverType getDriver(String driverName) {
        return drivers.get(driverName);
    }

    public void save() throws IOException {
        this.config.set_last(LocalDateTime.now().toString());
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), this.config);
    }

    public DataSource getDataSource(AdminSource source) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(source.getUrl());
        ds.setUsername(source.getUsername());
        ds.setPassword(source.getPassword());

        return ds;
    }
}
