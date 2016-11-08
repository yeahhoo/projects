package com.example.rest.services;

import com.example.dtos.OauthUserDto;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aleksandr_Savchenko
 */
@RestController
public class OauthUserRestController {

    //@PreAuthorize("#oauth2.hasScope('create')")
    @RequestMapping(method = RequestMethod.POST, value = "/oauth_user/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createUser(@RequestBody OauthUserDto request) {
        return "user created: " + request;
    }

}
