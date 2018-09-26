package com.playground.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.constant.Parameters;
import com.playground.handler.ExceptionHandler;
import com.playground.message.ClientInfo;
import com.playground.message.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * This class open a socket connection for incoming client register request
 * By the way two ways messaging established.
 *
 * Created by Dogukan Duman on 17.09.2018.
 */
public class MessageSender {
    private final static Logger LOGGER = Logger.getLogger(MessageSender.class.getName());
    private Socket socket;
    private DataOutputStream out;
    private ClientInfo clientInfo;
    private ExceptionHandler exceptionHandler;
    public MessageSender(ClientInfo clientInfo, ExceptionHandler exceptionHandler) {
        LOGGER.info("MessageSender instance will be created for client: " + clientInfo.getName() + " and port:" + clientInfo.getPortId());
        this.clientInfo = clientInfo;
        this.exceptionHandler = exceptionHandler;
        LOGGER.info("MessageSender instance is connecting to client: " + clientInfo.getName() + " and port:" + clientInfo.getPortId());
        connect();
    }

    /** Connect to client*/
    private void connect() {
        try {
            socket = new Socket(Parameters.defaultPlayGroundHost, clientInfo.getPortId());
            out = new DataOutputStream(socket.getOutputStream());
            LOGGER.info("MessageSender instance has been connected to client:" + clientInfo.getName() + " and port:" + clientInfo.getPortId());
        } catch (IOException e) {
            exceptionHandler.handleError(clientInfo,e);
        }
    }
    /**Close connection gracefully*/
    public void closeConnection() {
        try {
            LOGGER.info("MessageSender for client:"+clientInfo+" will be close gracefully.");
            out.close();
            socket.close();
        } catch (IOException e) {
            exceptionHandler.handleError(clientInfo,e);
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
            exceptionHandler.handleError(clientInfo,e);
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
