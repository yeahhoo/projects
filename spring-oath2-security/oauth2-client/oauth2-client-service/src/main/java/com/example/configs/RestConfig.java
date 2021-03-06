package com.example.configs;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alexander Savchenko
 */
@RestController
public class RestConfig {

    private static final Logger LOG = LoggerFactory.getLogger(RestConfig.class);

    @Value("${security.oauth2.client.serverLogoutUri}")
    private String serverLogoutUri;

    @Value("${security.oauth2.client.clientLogoutUri}")
    private String clientLogoutUri;

    @Value("${security.oauth2.client.clientId}")
    private String clientName;

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping({"/requestLogout"})
    public void requestLogout(HttpServletRequest request, HttpServletResponse response, Principal principal) throws Exception {
        // todo find a way to pass parameters more secure
        SecurityContext securityContext = SecurityContextHolder.getContext();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) securityContext.getAuthentication().getDetails();
        StringBuilder url = new StringBuilder(serverLogoutUri);
        url.append("?user=").append(securityContext.getAuthentication().getName())
                .append("&client=").append(clientName)
                .append("&token=").append(details.getTokenValue())
                .append("&urlToReturn=").append(clientLogoutUri);

        response.sendRedirect(url.toString());
    }

    @RequestMapping({"/index"})
    public ModelAndView home() throws Exception {
        LOG.info("/index page requested");
        return createModelViewForPage("index", "home");
    }

    @RequestMapping({"/createClient"})
    public ModelAndView createClientPage() throws Exception {
        LOG.info("/createClient Page requested");
        return createModelViewForPage("create_oath_client", "createClient");
    }

    @RequestMapping({"/createUser"})
    public ModelAndView createUserPage() throws Exception {
        LOG.info("/createUser Page requested");
        return createModelViewForPage("create_oauth_user", "createUser");
    }

    /**
     * builds ModelAndView object filled with base attributes needed by UI.
     * @param pageName - name of server page (*.ftl name).
     * @param viewName - client view to associate initial state.
     * */
    private ModelAndView createModelViewForPage(String pageName, String viewName) {
        ModelAndView model = new ModelAndView(pageName);
        Map<String, Object> responseMap = new HashMap<>();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        responseMap.put("username", userName);
        responseMap.put("isLogined", !"anonymousUser".equals(userName));
        responseMap.put("viewName", viewName);
        try {
            model.addObject("jsonResponse", mapper.writeValueAsString(responseMap));
        } catch (Exception e) {
            LOG.error("Error occurred while serialization", e);
            throw new RuntimeException("Error occurred while serializing parameters for client", e);
        }
        return model;
    }

}
