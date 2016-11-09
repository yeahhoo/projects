package com.example.config;

import com.example.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Aleksandr_Savchenko
*/
// http://stackoverflow.com/questions/28908946/spring-security-oauth2-and-form-login-configuration
@Configuration
@EnableWebSecurity
@Order(-20)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserRepository userRepository;

    // http://localhost:8001/client/
    // http://localhost:9001/server/hello
    // http://localhost:8001/client/server/hello

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.formLogin().loginPage("/login").failureUrl("/login?auth_error=true").permitAll();
/*
        http.authorizeRequests().antMatchers("/hello").permitAll(); // for OATH2
        http.requestMatchers().antMatchers("/hello").and().anonymous(); // for simple security
*/
        http.requestMatchers().antMatchers("/login**", "/oauth/authorize", "/oauth/confirm_access", "/customLogout**")
                .and().authorizeRequests().anyRequest().authenticated();

        http.logout().invalidateHttpSession(true).clearAuthentication(true);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers("GET", "/resources/**"
                        , "/libs/**.js"
                        , "/libs-css/**.css"
                        , "/templates/**"
                        , "/ui/**"
                        , "/401.html"
                        , "/404.html"
                        , "/500.html"
                        , "/cors"
                        , "/hello");

        webSecurity.ignoring().antMatchers("POST", "/oauth_user/create");
    }


    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("dba").password("dba").roles("DBA");
        auth.inMemoryAuthentication().withUser("me").password("me").roles("DBA");
    }
    */

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userRepository)/*.passwordEncoder(passwordEncoder)*/;
    }


}
