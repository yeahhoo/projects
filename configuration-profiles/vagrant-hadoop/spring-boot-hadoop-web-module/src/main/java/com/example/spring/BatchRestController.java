package com.example.spring;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    @Qualifier("asyncJobLauncher")
    private JobLauncher asyncJobLauncher;

    @Bean
    public JobLauncher asyncJobLauncher() {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return jobLauncher;
    }

    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public String test() throws Exception {
        return "application is up and running";
    }

    @RequestMapping(value = "/runJob", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, String> runMapReduceJob(@RequestParam(required = false, defaultValue = "false") boolean isAsync,
                                               @RequestParam(required = false, defaultValue = "false") boolean isDebug) throws Exception {
        String inputPath = "/test.txt";
        String outputPath = "/output";

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("inputPath", inputPath)
                .addString("outputPath", outputPath)
                .addString("isDebug", String.valueOf(isDebug))
                .addDate("date", new Date())
                .toJobParameters();
        JobExecution execution = null;
        if (isAsync) {
            execution = asyncJobLauncher.run(mapReduceJob, jobParameters);
        } else {
            execution = jobLauncher.run(mapReduceJob, jobParameters);
        }
        Map<String, String> map = (Map<String, String>) execution.getExecutionContext().get("result");
        return map;
    }

}
