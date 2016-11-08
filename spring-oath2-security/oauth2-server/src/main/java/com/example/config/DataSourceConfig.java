package com.example.config;

import javax.sql.DataSource;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.SQLException;

/**
 * @author Aleksandr_Savchenko
 */
@Configuration
public class DataSourceConfig {

    @Value("${database.inMemoryDatabaseTcpPort}")
    private String inMemoryDatabaseTcpPort;

    @Value("${database.inMemoryDatabaseWebServerPort}")
    private String inMemoryDatabaseWebServerPort;

    @Value("${database.driverClass}")
    private String driverClass;

    @Value("${database.url}")
    private String url;

    @Value("${database.user}")
    private String user;

    @Value("${database.password}")
    private String password;

    @Bean(initMethod="start", destroyMethod="stop")
    public org.h2.tools.Server h2Server() throws SQLException {
        return org.h2.tools.Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", inMemoryDatabaseTcpPort);
    }

    @Bean(initMethod="start", destroyMethod="stop")
    @DependsOn(value = "h2Server")
    public org.h2.tools.Server h2WebServer() throws SQLException {
        Server server = org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", "-webPort", inMemoryDatabaseWebServerPort);
        return server;
    }

    @Bean
    @DependsOn(value = "h2Server")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        DatabasePopulatorUtils.execute(createDatabasePopulator(), dataSource);
        return dataSource;
    }

    private DatabasePopulator createDatabasePopulator() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(false);
        databasePopulator.addScript(new ClassPathResource("db/sql/create-db.sql"));
        databasePopulator.addScript(new ClassPathResource("db/sql/insert-data.sql"));
        return databasePopulator;
    }

}
