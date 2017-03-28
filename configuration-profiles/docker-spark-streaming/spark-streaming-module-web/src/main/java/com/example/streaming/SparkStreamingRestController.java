package com.example.streaming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aleksandr_Savchenko
 */
@RestController
public class SparkStreamingRestController {

    @Autowired
    private TcpServerBean tcpServer;

    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public String test() throws Exception {
        return "application is up and running";
    }

    @RequestMapping(value = "/startServer", method = {RequestMethod.GET, RequestMethod.POST})
    public void runServer() throws Exception {
        tcpServer.runServer();
    }

    @RequestMapping(value = "/stopServer", method = {RequestMethod.GET, RequestMethod.POST})
    public void stopServer() throws Exception {
        tcpServer.stopServer();
    }

}
