package com.example.spring;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Aleksandr_Savchenko
 */
@RestController
public class BatchRestController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job mapReduceJob;

    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public String test() throws Exception {
        return "application is up and running";
    }

    @RequestMapping(value = "/runJob", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, String> runMapReduceJob() throws Exception {
        String inputPath = "/test.txt";
        String outputPath = "/output";

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("inputPath", inputPath)
                .addString("outputPath", outputPath)
                .toJobParameters();

        JobExecution execution = jobLauncher.run(mapReduceJob, jobParameters);
        Map<String, String> map = (Map<String, String>) execution.getExecutionContext().get("result");
        return map;
    }


}
