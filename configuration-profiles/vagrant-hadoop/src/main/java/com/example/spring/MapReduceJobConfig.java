package com.example.spring;

import com.example.WordCount;
import com.example.WordCountMapper;
import com.example.WordCountReducer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.hadoop.fs.FileStatus;
import org.springframework.data.hadoop.mapreduce.ToolRunner;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.hadoop.HadoopSystemConstants;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.data.hadoop.mapreduce.JobRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.batch.api.listener.JobListener;

/**
 * @author Aleksandr_Savchenko
 */
@PropertySource("classpath:batch.properties")
@Configuration
public class MapReduceJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilders;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    private org.apache.hadoop.conf.Configuration configuration;

    @Autowired
    org.apache.hadoop.mapreduce.Job myjob1;

    @Autowired
    private ToolRunner toolRunner;

    @Bean(name = HadoopSystemConstants.DEFAULT_ID_FSSHELL)
    public FsShell fsShell() {
        return new FsShell(configuration);
    }

    @Bean
    public Job mapReduceJob() {
        return jobBuilders.get("mapReduceJob")
                .start(clearFolderStep())
                .next(mapReduceStep())
                .listener(jobExecListener())
                .build();
    }

    @Bean
    public JobExecutionListener jobExecListener() {
        return new JobFinishListener();
    }

    @Bean
    public Step clearFolderStep() {
        return stepBuilders.get("clearFolderStep")
                .tasklet(clearFolderTasklet())
                .build();
    }

    @Bean
    public Step mapReduceStep() {
        return stepBuilders.get("mapReduceStep")
                .tasklet(mapReduceTasklet())
                .build();
    }

    @Bean
    public Tasklet clearFolderTasklet() {
        return new ClearFolderTasklet();
    }

    @Bean
    public Tasklet mapReduceTasklet() {
        return new MapReduceTasklet();
    }

    private class ClearFolderTasklet implements Tasklet {

        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
            String outputFolder = (String) jobParameters.get("outputPath");
            System.out.println("Executing ClearFolderTasklet: removing " + outputFolder);
            if (fsShell().test(outputFolder)) {
                fsShell().rmr(outputFolder);
            }
            return RepeatStatus.FINISHED;
        }
    }

    private class MapReduceTasklet implements Tasklet {

        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
            String inputPath = (String) jobParameters.get("inputPath");
            String outputPath = (String) jobParameters.get("outputPath");
            System.out.println("Executing MapReduceTasklet: map-reducing " + inputPath);
            JobRunner jobLauncher = new JobRunner();
            jobLauncher.setRunAtStartup(false);
            jobLauncher.setJob(myjob1);
            jobLauncher.call();
            /*
            toolRunner.setArguments(inputPath, outputPath);
            toolRunner.call();
            */
            // int res = ToolRunner.run(new WordCount(), new String[] {inputPath, outputPath});
            return RepeatStatus.FINISHED;
        }
    }

    private class JobFinishListener implements JobExecutionListener {

        @Override
        public void beforeJob(JobExecution jobExecution) {
            System.out.println("do nothing");
        }

        @Override
        public void afterJob(JobExecution jobExecution) {
            String outputFolder = jobExecution.getJobParameters().getString("outputPath");
            System.out.println("Executing ReadResultTasklet: reading " + outputFolder);
            List<String> results = Lists.newArrayList();
            List<FileStatus> files = new ArrayList<FileStatus>(fsShell().ls(outputFolder));
            for (FileStatus file : files) {
                if (file.isFile() && file.getLen() > 0) {
                    results.addAll(fsShell().text(file.getPath().toUri().getPath()));
                }
            }
            Map<String, String> map = Maps.newHashMap();
            if (!results.isEmpty()) {
                String csv = results.get(0);
                String[] pairs = csv.split("\n");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("\t");
                    map.put(keyValue[0], keyValue[1]);
                }
            }
            jobExecution.getExecutionContext().put("result", map);
        }
    }

}
