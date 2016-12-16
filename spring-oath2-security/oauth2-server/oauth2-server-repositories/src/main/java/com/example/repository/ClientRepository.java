package com.example.repository;

import com.example.config.DataSourceConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * @author Aleksandr_Savchenko
 */
@Repository
@Import({DataSourceConfig.class})
public class ClientRepository extends JdbcClientDetailsService {

    @Autowired
    public ClientRepository(DataSource dataSource) {
        super(dataSource);
    }
}
