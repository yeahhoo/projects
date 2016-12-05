package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Aleksandr_Savchenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationTests {

    @Autowired
    private TestRestTemplate template;

    @Value("${server.port}")
    private int port;

    @Test
    public void homePageProtected() {
        ResponseEntity<String> response = template.getForEntity("/server/", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void authorizationRedirects() {
        String serverUrl = "http://localhost:" + port + "/server/";
        String authUrl = serverUrl + "oauth/authorize";
        String loginUrl = serverUrl + "login";
        ResponseEntity<String> response = template.getForEntity(authUrl, String.class);
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        String location = response.getHeaders().getFirst("Location");
        assertTrue("Wrong header: " + location, location.startsWith(loginUrl));
    }

    @Test
    public void loginSucceeds() {
        String serverUrl = "http://localhost:" + port + "/server/";
        String loginUrl = serverUrl + "login";
        ResponseEntity<String> response = template.getForEntity(loginUrl, String.class);
        String csrf = getCsrf(response.getBody());
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.set("username", "me");
        form.set("password", "me");
        form.set("_csrf", csrf);
        HttpHeaders headers = new HttpHeaders();
        headers.put("COOKIE", response.getHeaders().get("Set-Cookie"));
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<MultiValueMap<String, String>>(
                form, headers, HttpMethod.POST, URI.create(loginUrl)
        );
        ResponseEntity<Void> location = template.exchange(request, Void.class);
        assertEquals(serverUrl, location.getHeaders().getFirst("Location"));
    }

    private String getCsrf(String soup) {
        Matcher matcher = Pattern.compile("(?s).*name=\"_csrf\".*?value=\"([^\"]+).*")
                .matcher(soup);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }

}
