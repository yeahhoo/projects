package com.example.rest.services;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Aleksandr_Savchenko
 */
@RestController
@Api(value = "commonController")
public class CommonRestController {

    @RequestMapping({"/user", "/me"})
    public Map<String, String> user(Principal principal) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", principal.getName());
        return map;
    }

    //@CrossOrigin(origins = {"http://localhost:8001"})
    @RequestMapping({"contact"})
    public String contact() {
        return "Contact me in case of any problems or don't contact, as you wish."
               + "\nHere is my fake email sending messages to you will reach nobody: buddy@coolcompany.com"
               + "\nPlease note that the contact request work via use of CORS, cool, ha.";
    }

    @RequestMapping({"about"})
    public String about() {
        return "This application is about learning something new and doesn't bring any sense."
               + "\nThe mission was to create application with use of Spring OAuth2 and React."
               + "\nOn my mind result is not so bad. At least it contains some solutions and cheat sheets developers need in everyday life.";
    }

    @RequestMapping({"hellouser"})
    public String helloUser(OAuth2Authentication auth) {
        return "Hello: " + auth.getName();
    }


}
