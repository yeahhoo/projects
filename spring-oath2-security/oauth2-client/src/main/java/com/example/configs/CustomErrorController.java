package com.example.configs;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
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
            return model;
        }
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

}
