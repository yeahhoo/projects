package com.example.rest.services;

import com.example.dtos.OauthCreationDto;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Aleksandr_Savchenko
 */
@RestController
public class OauthClientRestController {

    @RequestMapping({"scopes"})
    public List<String> getAvailableScopes() {
        return Arrays.asList("openid, read, create");
    }

    @PreAuthorize("#oauth2.hasScope('read')")
    @RequestMapping(method = RequestMethod.GET, value = "/oauth_client/read")
    public String readClient(String id) {
        return "read: " + id;
    }

    @PreAuthorize("#oauth2.hasScope('create')")
    @RequestMapping(method = RequestMethod.POST, value = "/oauth_client/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createClient(@RequestBody OauthCreationDto request) {
        return "client created: " + request;
    }
}
