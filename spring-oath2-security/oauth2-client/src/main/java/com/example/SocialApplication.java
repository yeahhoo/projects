/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@SpringBootApplication
@EnableOAuth2Sso
@EnableZuulProxy
public class SocialApplication extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        //SpringApplication.run(SocialApplication.class, args);
        new SpringApplicationBuilder(SocialApplication.class).properties("spring.config.name=client").run(args);
        //new SpringApplicationBuilder(SocialApplication.class).properties("spring.config.name=uaa").run(args);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.logout().and()
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/index.html", "/home.html", "/", "/login**", "/webjars/**", "/resources/**", "/server/**")
                .permitAll().anyRequest().authenticated();
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.logout().invalidateHttpSession(true).clearAuthentication(true).logoutUrl("/mylogout").logoutSuccessUrl("/");
    }

}

