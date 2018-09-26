package com.playground.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.message.Message;
import com.playground.service.MessageListener;

import java.io.IOException;
import java.time.Instant;

/**
 * Created by Dogukan Duman on 17.09.2018.
 */
public class MessageProducer {
    private static MessageHandler messageHandler;

    public static Instant lastMessagingDate = Instant.now();;

    public static void register(MessageHandler messageHandler) {
        MessageProducer.messageHandler = messageHandler;
    }

    public static void produce(String rawMessage, MessageListener listener) {
        lastMessagingDate = Instant.now();

        ObjectMapper mapper = new ObjectMapper();
        Message message = null;
        try {
            message = mapper.readValue(rawMessage, Message.class);

            if (message.getMessageType() == 0) {
                /**ADD ME */
                produceRegisterEvent(message, listener);
            } else if (message.getMessageType() == 1) {
                /**DELETE ME */
                produceUnRegisterEvent(message);
            } else if (message.getMessageType() == 2) {
                /**NORMAL MESSAGE */
                produceTestMessage(message);

            } else if (message.getMessageType() == 3) {
                /**INFORMATION MESSAGE */
                produceTestMessage(message);

            }

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    private static void produceRegisterEvent(Message message, MessageListener listener) {
        if (messageHandler != null) {
            listener.setClientInfo(message.getClientInfo());
            messageHandler.registerEvent(message, listener);
        }

    }

    private static void produceUnRegisterEvent(Message message) {
        if (messageHandler != null) {
            messageHandler.unRegisterEvent(message.getClientInfo());
        }

    }


    private static void produceTestMessage(Message message) {
        if (messageHandler != null) {
            messageHandler.handleText(message);
        }

    }

    public static Instant getLastMessagingDate() {
        return lastMessagingDate;
    }
}
