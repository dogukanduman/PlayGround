package com.playground.service;

import com.playground.handler.ExceptionHandler;
import com.playground.handler.MessageProducer;
import com.playground.message.ClientInfo;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * MessageListener listens coming messages from client.
 * It forwards the messages to {@link MessageProducer} for processing
 * Later {@link PlayGroundServiceImp} handles these messages.(Observer pattern)
 * Created by Dogukan Duman on 17.09.2018.
 */
public class MessageListener extends Thread {

    private final static Logger LOGGER = Logger.getLogger(MessageListener.class.getName());

    private Socket socket;
    private DataInputStream in = null;
    private ClientInfo clientInfo;
    private boolean isContinue = true;
    private ExceptionHandler exceptionHandler;


    public MessageListener(Socket clientSocket,ExceptionHandler exceptionHandler) {

        /**Create MessageListener after every  serverSocket.accept() call in {@link PlayGroundServiceImp}
         * After parsing message this instance will link with the related {@link ClientInfo}
         **/
        this.socket = clientSocket;
        this.exceptionHandler = exceptionHandler;
        LOGGER.info("MessageListener instance will be created");
        init();
    }

    public void run() {


        String line = "";
        try {
            while (isContinue) {
            /**Read next incoming message */
                line = in.readUTF();
                /**Send it to MessageProducer */
                MessageProducer.produce(line, this);

            }
        } catch (IOException e) {
            LOGGER.info(clientInfo.getName()+" seems closed. Connection will close.");
            exceptionHandler.handleError(clientInfo,e);
        }
    }

    private void init() {
        try {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            LOGGER.info("MessageListener is created,it is ready for new messages");
        } catch (IOException e) {
            exceptionHandler.handleError(clientInfo,e);
        }
    }

    /**
     * Close connection gracefully
     */
    public void closeConnection() {
        try {
            LOGGER.info("MessageListener for client:" + clientInfo + " will be close gracefully.");
            in.close();
            isContinue = false;
        } catch (IOException e) {
            exceptionHandler.handleError(clientInfo,e);
        }
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }
}