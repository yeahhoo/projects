package com.example.repositories;

import com.example.config.DataSourceConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * @author Aleksandr_Savchenko
 */
@Repository
@Import({DataSourceConfig.class})
public class UserRepository extends JdbcUserDetailsManager {

    /*
    SELECT u.*, r.role FROM USERS u
LEFT JOIN ROLES r ON u.username = r.username
WHERE u.USERNAME = 'me'
    * */

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        setDataSource(dataSource);
        setCreateUserSql("INSERT INTO users (id, username, password, enabled) VALUES (user_sequence.nextVal, ?, ?, ?)");
    }

}
