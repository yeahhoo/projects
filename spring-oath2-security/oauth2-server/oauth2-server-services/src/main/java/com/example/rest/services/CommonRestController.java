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
    @RequestMapping({"cors"})
    public String myCors() {
        return "Hey, cors works, wah";
    }

    @RequestMapping({"hello"})
    public String hello() {
        return "Hello buddy";
    }

    @RequestMapping({"hellouser"})
    public String helloUser(OAuth2Authentication auth) {
        return "Hello: " + auth.getName();
    }


}
