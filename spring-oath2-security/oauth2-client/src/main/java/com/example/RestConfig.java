package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alexander Savchenko
 */

@RestController
public class RestConfig {

    @Value("${security.oauth2.client.serverLogoutUri}")
    private String serverLogoutUri;

    @Value("${security.oauth2.client.clientLogoutUri}")
    private String clientLogoutUri;

    @RequestMapping({"/requestLogout"})
    public void requestLogout(HttpServletRequest request, HttpServletResponse response, Principal principal) throws Exception {
        // todo find a way to pass parameters more secure
        SecurityContext securityContext = SecurityContextHolder.getContext();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) securityContext.getAuthentication().getDetails();
        StringBuilder url = new StringBuilder(serverLogoutUri);
        url.append("?user=").append(securityContext.getAuthentication().getName())
                .append("&token=").append(details.getTokenValue())
                .append("&urlToReturn=").append(clientLogoutUri);

        response.sendRedirect(url.toString());
    }


}
