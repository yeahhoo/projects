package com.example.streaming;

import com.google.common.collect.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * @author Aleksandr_Savchenko
 */
@RestController
public class SparkStreamingRestController {


    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public String test() throws Exception {
        return "application is up and running";
    }

    @RequestMapping(value = "/runJob", method = {RequestMethod.GET, RequestMethod.POST})
    public void runMapReduceJob() throws Exception {
        Map<String, Integer> map = Maps.newHashMap();
        String msgcont = "test message";
        String webServer = "web-app";
        System.out.println(tcpSend(webServer, 9999, msgcont));
    }

    public static String tcpSend(String ip, int port, String content) {
        String modifiedSentence;
        Socket clientSocket;
        try {
            clientSocket = new Socket(ip, port);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            //BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer.writeBytes(content + '\n');
            //modifiedSentence = inFromServer.readLine();
            //System.out.println("FROM SERVER: " + modifiedSentence);
            clientSocket.close();
        }
        catch (Exception exc) {
            return "fail";
        }
        return "success";
    }

    @RequestMapping(value = "/runServer", method = {RequestMethod.GET, RequestMethod.POST})
    public void runServer() throws Exception {
        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(9999);

        while(true) {
            try {
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                Socket outputSocket = new Socket("web-app", 9999);
                DataOutputStream outToClient = new DataOutputStream(outputSocket.getOutputStream());
                clientSentence = inFromClient.readLine();
                System.out.println("Received: " + clientSentence);
                capitalizedSentence = clientSentence.toUpperCase() + '\n';
                outToClient.writeBytes(capitalizedSentence);
            } catch (Exception e) {
                System.out.println("well, something goes wrong");
            }

        }
    }


}
