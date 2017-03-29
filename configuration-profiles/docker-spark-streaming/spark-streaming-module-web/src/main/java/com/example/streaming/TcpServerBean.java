package com.example.streaming;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Aleksandr_Savchenko
 */
@Component
public class TcpServerBean {

    private ServerSocket serverSocket;
    private Socket sparkSocket;
    private ExecutorService exec;

    @PostConstruct
    public void initTcpServer() {
        exec = Executors.newSingleThreadExecutor();
        runServer();
    }

    @PreDestroy
    public void closeTcpServer() {
        stopServer();
        exec.shutdownNow();
    }

    public void runServer() {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                initSockets();
            }
        });
    }

    private void initSockets() {
        try {
            serverSocket = new ServerSocket(9999);
            while (true) {
                Socket connectionSocket = serverSocket.accept();
                if (!connectionSocket.getLocalAddress().equals(connectionSocket.getInetAddress())) {
                    // another host - it's spark connection
                    sparkSocket = connectionSocket;
                } else if (sparkSocket != null) {
                    // current host - routing message
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    String clientSentence = inFromClient.readLine();
                    DataOutputStream outToClient = new DataOutputStream(sparkSocket.getOutputStream());
                    outToClient.writeBytes(clientSentence + "\n");
                    connectionSocket.close();
                }
            }
        } catch (Exception e) {
            stopServer();
            throw new RuntimeException("well, something goes wrong", e);
        }
    }

    public void stopServer() {
        try {
            if (sparkSocket != null) {
                sparkSocket.close();
                sparkSocket = null;
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            exec.awaitTermination(1l, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("couldn't close connect", e);
        }
    }

}
