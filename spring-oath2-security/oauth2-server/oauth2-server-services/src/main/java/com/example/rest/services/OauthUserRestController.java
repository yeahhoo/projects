package com.example.rest.services;

import com.example.dtos.OauthUserDto;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

import java.util.Arrays;

/**
 * @author Aleksandr_Savchenko
 */
@RestController
@Api(value = "userController")
public class OauthUserRestController {

    @Autowired
    private UserRepository userRepository;

    //@PreAuthorize("#oauth2.hasScope('create')")
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    @RequestMapping(method = RequestMethod.POST, value = "/oauth_user/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody OauthUserDto request) {
        User user = new User(request.getUser(), request.getPassword(), true, true, true, true, Arrays.asList(new SimpleGrantedAuthority("USER")));
        userRepository.createUser(user);
        return user;
    }

}
