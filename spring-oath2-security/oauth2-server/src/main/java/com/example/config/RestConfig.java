package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aleksandr_Savchenko
 */
@RestController
@EnableResourceServer
@Import({InnerConfig.class})
public class RestConfig {

    private static final String BEARER_AUTHENTICATION = "bearer ";
    private static final String HEADER_AUTHORIZATION = "authorization";

    @Autowired
    private TokenStore tokenStore;

    @RequestMapping("/auth")
    @ResponseBody
    public Principal doAccept(Principal user) {
        return user;
    }


    @RequestMapping(value = "/customLogout", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView logoutPage(HttpServletRequest request, HttpServletResponse response, Principal principal) throws Exception {
        ModelAndView modelAndView = new ModelAndView("custom_logout");
        modelAndView.addObject("token", request.getParameter("token"));
        modelAndView.addObject("urlToReturn", request.getParameter("urlToReturn"));
        modelAndView.addObject("user", request.getParameter("user"));
        return modelAndView;
    }


    // http://websystique.com/spring-security/spring-security-4-logout-example/
    @RequestMapping(value = "/mylogout", method = {RequestMethod.GET, RequestMethod.POST})
    public Boolean logout(HttpServletRequest request, HttpServletResponse response, Principal principal) throws Exception {
        String token = request.getHeader(HEADER_AUTHORIZATION);
        if (token != null && token.startsWith(BEARER_AUTHENTICATION)) {
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token.split(" ")[1]);
            if (oAuth2AccessToken != null) {
                tokenStore.removeAccessToken(oAuth2AccessToken);
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                    new SecurityContextLogoutHandler().logout(request, response, auth);
                }
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

}
