package com.example;

import com.example.filth.HeaderMapRequestWrapper;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SpringBootApplication
@EnableOAuth2Sso
@EnableZuulProxy
public class SocialApplication extends WebSecurityConfigurerAdapter {

    public static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    public static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";

    public static final String[] NON_SECURED_URLS = new String[] {
            "/",
            "/index.ftl",
            "/home.ftl",
            "/login**",
            "/resources/**",
            "/libs/**",
            "/components/**",
            "/server/**"
    };

    public static void main(String[] args) {
        //SpringApplication.run(SocialApplication.class, args);
        new SpringApplicationBuilder(SocialApplication.class).properties("spring.config.name=client").run(args);
        //new SpringApplicationBuilder(SocialApplication.class).properties("spring.config.name=uaa").run(args);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.logout().and()
                .antMatcher("/**").authorizeRequests()
                .antMatchers(NON_SECURED_URLS)
                .permitAll().anyRequest().authenticated();

        //http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.csrf().csrfTokenRepository(csrfTokenRepository()).and()
                .addFilterBefore(csrfHeaderFilter(), CsrfFilter.class);

        http.logout().invalidateHttpSession(true).clearAuthentication(true).logoutUrl("/mylogout").logoutSuccessUrl("/");
    }


    /**
     * security protocol was changed in order to rely only on user's session. It used to be:
     *     1) server gets value of the token from session;
     *     2) server extracts value of the header from request;
     *     3) compare them to make sure that user has sent it.
     * It makes client code put the header 'X-XSRF-TOKEN' with the same value as cookie 'XSRF-TOKEN' to pass the barrier.
     * It doesn't secure user because if hacker finds out the cookie he will tamper it easily.
     * So it was disabled for the sake of simple client code. Now user will rely only on session.
     *
     * the source: org.springframework.security.web.csrf.CsrfFilter
     * */
    @Bean
    Filter csrfHeaderFilter() {

        return new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {

                HeaderMapRequestWrapper reqWrapper = new HeaderMapRequestWrapper(request);
                CsrfToken csrf = csrfTokenRepository().loadToken(request);
                if (csrf != null) {
                    request.setAttribute(CsrfToken.class.getName(), csrf);
                    request.setAttribute(csrf.getParameterName(), csrf);
                    String token = csrf.getToken();
                    Cookie cookie = WebUtils.getCookie(request, CSRF_COOKIE_NAME);
                    if (cookie == null || (cookie != null && !token.equals(cookie.getValue()))) {
                        cookie = new Cookie(CSRF_COOKIE_NAME, token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                    reqWrapper.putHeader(CSRF_HEADER_NAME, token);
                }
                filterChain.doFilter(reqWrapper, response);
            }
        };
    }

    @Bean
    CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName(CSRF_HEADER_NAME);
        return repository;
    }


}

