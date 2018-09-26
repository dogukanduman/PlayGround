package com.player.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.player.constant.Parameters;
import com.player.handler.ExceptionHandler;
import com.player.message.ClientInfo;
import com.player.message.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by Dogukan Duman on 17.09.2018.
 */
public class MessageSender {



    private final static Logger LOGGER = Logger.getLogger(MessageSender.class.getName());
    private Socket socket;
    private DataOutputStream out;
    private ClientInfo serverInfo;
    private ExceptionHandler exceptionHandler;
    public MessageSender(ClientInfo serverInfo, ExceptionHandler exceptionHandler) {
        LOGGER.info("MessageSender instance will be created for server: " + serverInfo.getName() + " and port:" + serverInfo.getPortId());
        this.serverInfo = serverInfo;
        this.exceptionHandler=exceptionHandler;
        LOGGER.info("MessageSender instance is connecting to server: " + serverInfo.getName() + " and port:" + serverInfo.getPortId());
        connect();
    }

    /** Connect to client*/
    private void connect() {
        try {
            socket = new Socket(Parameters.defaultPlayGroundHost, serverInfo.getPortId());
            out = new DataOutputStream(socket.getOutputStream());
            LOGGER.info("MessageSender instance has been connected to server:" + serverInfo.getName() + " and port:" + serverInfo.getPortId());
        } catch (IOException e) {
            exceptionHandler.handle(new Exception("MessageSender could not connect to PlayGroundService"));
        }
    }
    /**Close connection gracefully*/
    public void closeConnection() {
        try {
            LOGGER.info("MessageSender for client:"+serverInfo+" will be close gracefully.");
            if (out != null) {
                out.close();
                socket.close();
            }
        } catch (IOException e) {
            exceptionHandler.handle(e);
        }
    }

    /**Send messages to client whenever need*/
    public void sendMessage(Message message) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonInString = mapper.writeValueAsString(message);
            // LOGGER.info("MessageSender is sending message msg:"+jsonInString);
            out.writeUTF(jsonInString);
        } catch (JsonProcessingException e) {
            exceptionHandler.handle(e);
        } catch (IOException e) {
            exceptionHandler.handle(e);
        }
    }
}
