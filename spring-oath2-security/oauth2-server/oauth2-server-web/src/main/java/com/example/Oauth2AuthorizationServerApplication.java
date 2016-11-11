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

    // todo create maven UI module
    // todo find a way to optimize 'use strict'
    // todo port other token services on JDBC
    // todo write JUnits
    // todo refactor client side on AngularJs or ReactJs
    // todo include maven Js building plugin
    // todo include Docker
}