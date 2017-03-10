package com.example.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Aleksandr_Savchenko
 */
@RestController
public class SparkRestController {

    @Autowired
    private SparkService sparkService;

    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public String test() throws Exception {
        return "application is up and running";
    }

    @RequestMapping(value = "/runJob", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Integer> runMapReduceJob(@RequestParam(required = false, defaultValue = "test1.txt") String input,
                                               @RequestParam(required = false, defaultValue = "output") String output) throws Exception {
        Map<String, Integer> map = sparkService.analyzeData(input, output);
        return map;
    }

}
