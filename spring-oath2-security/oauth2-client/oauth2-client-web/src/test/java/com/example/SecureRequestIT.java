package com.example;

import static org.junit.Assert.assertEquals;

import com.google.common.io.BaseEncoding;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Aleksandr_Savchenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"spring.config.location = classpath:client-test.yml"})
public class SecureRequestIT {

    private static final Logger logger = LoggerFactory.getLogger(SecureRequestIT.class);

    @Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    @Value("${test.username}")
    private String username;

    @Value("${test.password}")
    private String password;

    @Value("${test.user-url}")
    private String userUrl;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void secureRequest() throws Exception {
        logger.info("Integration tests launched");

        //curl -H "Authorization: Basic Zm9vOmZvb3NlY3JldA==" -POST localhost:9000/server/oauth/token -d grant_type=password -d username=me -d password=me
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.set("username", username);
        form.set("password", password);
        form.set("grant_type", "password");

        HttpHeaders loginHeaders = new HttpHeaders();
        String clientPasswordPair = clientId + ":" + clientSecret;
        String codedAuth = BaseEncoding.base64().encode(clientPasswordPair.getBytes("UTF-8"));
        String authHeader = "Basic " + codedAuth;
        loginHeaders.put("Authorization", Arrays.asList(authHeader));
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<MultiValueMap<String, String>>(
                form, loginHeaders, HttpMethod.POST, URI.create(accessTokenUri)
        );
        ResponseEntity<String> tokenResponse = template.exchange(request, String.class);
        assertEquals(HttpStatus.OK, tokenResponse.getStatusCode());
        Jackson2JsonParser parser = new Jackson2JsonParser();
        Map<String, Object> responseMap = parser.parseMap(tokenResponse.getBody());

        //curl -H "Authorization: Bearer %TOKEN%" http://localhost:9001/server/user
        String authBearerHeader = String.format("Bearer %s", responseMap.get("access_token"));
        loginHeaders.put("Authorization", Arrays.asList(authBearerHeader));
        RequestEntity<MultiValueMap<String, String>> userRequest = new RequestEntity<MultiValueMap<String, String>>(
                null, loginHeaders, HttpMethod.POST, URI.create(userUrl)
        );
        ResponseEntity<String> userResponse = template.exchange(userRequest, String.class);
        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertEquals(username, parser.parseMap(userResponse.getBody()).get("name"));
    }

}
