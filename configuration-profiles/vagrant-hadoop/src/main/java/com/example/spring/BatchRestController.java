package com.example.spring;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.mapreduce.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.data.hadoop.HadoopSystemConstants;
import org.springframework.data.hadoop.fs.FsShell;

import org.springframework.data.hadoop.mapreduce.JobRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Aleksandr_Savchenko
 */
@RestController
public class BatchRestController {

/*
    @Autowired
    JobRunner jobLauncher;
*/
    @Autowired
    Job myjob1;

    @Autowired
    private Configuration configuration;

    @Bean(name = HadoopSystemConstants.DEFAULT_ID_FSSHELL)
    public FsShell fsShell() {
        return new FsShell(configuration);
    }

    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public String test() throws Exception {
        return "application is up and running";
    }

    @RequestMapping(value = "/runJob", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, String> runMapReduceJob() throws Exception {
        String inputPath = "/test.txt";
        String outputPath = "/output";
        //int res = ToolRunner.run(new WordCount(), new String[] {inputPath, outputPath});

        JobRunner jobLauncher = new JobRunner();
        jobLauncher.setRunAtStartup(false);
        jobLauncher.setJob(myjob1);

        List<Callable<?>> preActions = new ArrayList<Callable<?>>();
        preActions.add(new PreActionCallable(outputPath));
        jobLauncher.setPreAction(preActions);
        jobLauncher.call();

        Map<String, String> map = readResult(outputPath);
        return map;
    }

    private Map<String, String> readResult(String outputPath) {
        List<String> results = Lists.newArrayList();
        List<FileStatus> files = new ArrayList<FileStatus>(fsShell().ls(outputPath));
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

        return map;
    }

    private class PreActionCallable implements Callable<Object> {

        private String outputFolder;

        public PreActionCallable(String outputFolder) {
            this.outputFolder = outputFolder;
        }

        @Override
        public Object call() throws Exception {
            if (fsShell().test(outputFolder)) {
                fsShell().rmr(outputFolder);
                return true;
            }
            return false;
        }
    }

}
