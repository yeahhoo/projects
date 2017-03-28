package com.example.streaming;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Aleksandr_Savchenko
 */
@Component
@DependsOn(value = {"tcpServerBean"})
public class TcpMessageSender {

    public static final String IP = "web-app";
    public static final int PORT = 9999;
    private ExecutorService exec;

    @PostConstruct
    public void initTcpSender() throws Exception {
        exec = Executors.newSingleThreadExecutor();
        exec.submit(new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                while (true) {
                    try {
                        for (int i = 0; i < 40; i++) {
                            tcpSend(r.nextInt(100));
                        }
                        Thread.sleep(TimeUnit.SECONDS.toMillis(1l));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @PreDestroy
    public void shutdown() {
        exec.shutdownNow();
    }

    public static boolean tcpSend(int value) throws Exception {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(IP, PORT);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(String.valueOf(value));
            clientSocket.close();
        } catch (Exception exc) {
            if (clientSocket != null) {
                clientSocket.close();
            }
            return true;
        }
        return false;
    }

}
