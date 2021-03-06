package com.example.rest.services;

import com.example.repository.ClientRepository;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

import java.util.Arrays;
import java.util.List;

/**
 * @author Aleksandr_Savchenko
 */
@RestController
@Api(value = "clientController")
public class OauthClientRestController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

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
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    public ClientDetails createClient(@RequestBody BaseClientDetails clientDetails) {
        clientRepository.addClientDetails(clientDetails);
        return clientDetails;
    }

    /**
     * the method just tests transaction support - user "testTransaction" must not be created, the transaction must be roll-backed.
     * */
    @RequestMapping(method = RequestMethod.POST, value = "/oauth_client/exception", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    public String testException() {
        userRepository.createUser(new User("testTransaction", "testTransaction", true, true, true, true, Arrays.asList(new SimpleGrantedAuthority("USER"))));
        throw new RuntimeException("oouh, sorry man");
    }
}
