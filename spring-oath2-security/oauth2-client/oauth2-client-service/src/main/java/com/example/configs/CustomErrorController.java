package com.example.configs;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aleksandr_Savchenko
 */
@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomErrorController.class);

    public static final String JSON_TYPE = "application/json";
    public static final String CONTENT_TYPE_HEADER = "content-type";

    @Value("${debug}")
    private boolean debug;

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return "/error";
    }

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping("/error")
    public Object error(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> errorAtrs = getErrorAttributes(request, debug);
        String contentType = request.getHeader(CONTENT_TYPE_HEADER);
        boolean isJsonRequest = StringUtils.isNotBlank(contentType) && contentType.startsWith(JSON_TYPE) ? true : false;
        if (isJsonRequest) {
            MappingJackson2JsonView model = new MappingJackson2JsonView();
            model.setAttributesMap(errorAtrs);
            return model;
        } else {
            ModelAndView model = new ModelAndView("custom_error");
            model.addAllObjects(errorAtrs);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                model.addObject("isLogined", false);
                model.addObject("username", StringUtils.EMPTY);
            } else {
                model.addObject("isLogined", !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName()));
                model.addObject("username", SecurityContextHolder.getContext().getAuthentication().getName());
            }
            model.addObject("viewName", "error");
            try {
                model.addObject("jsonResponse", mapper.writeValueAsString(model.getModel()));
            } catch (Exception e) {
                LOG.error("Error occurred while serialization in CustomErrorController", e);
                throw new RuntimeException("Error occurred while serializing parameters for client", e);
            }
            return model;
        }
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

}
