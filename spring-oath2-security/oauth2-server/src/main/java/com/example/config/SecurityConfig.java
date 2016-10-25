package com.example.config;

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
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("dba").password("dba").roles("DBA");
        auth.inMemoryAuthentication().withUser("me").password("me").roles("DBA");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers("GET", "/resources/**"
                        , "/templates/**"
                        , "/ui/**"
                        , "/401.html"
                        , "/404.html"
                        , "/500.html"
                        , "/cors"
                        , "/hello");

        //webSecurity.ignoring().antMatchers("POST", "/oauth_client/create");
    }


    // TODO CHANGE FOR BASE AUTHORISATION
/*
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                User user = userRepository.findOneByUsername(s);

                if (null == user) {
                    // leave that to be handled by log listener
                    throw new UsernameNotFoundException("The user with email " + s + " was not found");
                }

                return (UserDetails) user;
            }
        }).passwordEncoder(passwordEncoder);
    }
*/

}
