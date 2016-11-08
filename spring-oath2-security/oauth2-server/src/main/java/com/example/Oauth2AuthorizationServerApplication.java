package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.SessionAttributes;

@SpringBootApplication
@SessionAttributes("authorizationRequest")
public class Oauth2AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2AuthorizationServerApplication.class, args);
    }

    // todo add clients and users dynamically
    // todo refactor client side on AngularJs or ReactJs
    // todo include maven Js building plugin
    // todo include inmemory database
    // todo include Docker
}
