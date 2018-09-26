package com.player.service;

import com.player.handler.ExceptionHandler;
import com.player.handler.MessageProducer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;

/**
 * MessageListener listens coming messages from PlayGroundService.
 * Handles to informative message from PlayGroundService
 * It forwards the messages to {@link MessageProducer} for processing
 * Created by Dogukan Duman on 17.09.2018.
 */
public class MessageListener extends Thread {

    private final static Logger LOGGER = Logger.getLogger(MessageListener.class.getName());

    private Socket socket;
    private DataInputStream in = null;
    private boolean isContinue = true;
    private int port = 0;

    private ServerSocket serverSocket = null;

    private ExceptionHandler exceptionHandler;

    public MessageListener(int port,ExceptionHandler exceptionHandler) {
        this.port = port;
        this.exceptionHandler = exceptionHandler;
        LOGGER.info("MessageListener instance will be created for port:" + port);
    }

    public void run() {

        init();
        String line = "";
        try {
            while (isContinue) {

                /**Read next incoming message */
                line = in.readUTF();
                /**Send it to MessageProducer */
                MessageProducer.produce(line);

            }
        } catch (IOException e) {
            exceptionHandler.handle(e);
        }
    }

    public void init() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(3000);
            socket = serverSocket.accept();
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            LOGGER.info("MessageListener is created,it is ready for new messages from PlayGroundService");
        } catch (SocketTimeoutException e) {
            exceptionHandler.handle(e);
        } catch (IOException e) {
            exceptionHandler.handle(e);
        }
    }

    /**
     * Close connection gracefully
     */
    public void closeConnection() {
        try {
            LOGGER.info("MessageListener for PlayGroundService will be closed gracefully.");
            isContinue = false;
            if (in != null) {
                in.close();
                socket.close();
                serverSocket.close();
            }
        } catch (IOException e) {
            exceptionHandler.handle(e);
        }
    }

}