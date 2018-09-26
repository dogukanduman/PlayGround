package com.player.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.player.message.Message;

import java.io.IOException;

/**
 * Created by Dogukan Duman on 17.09.2018.
 */
public class MessageProducer {

    private static MessageHandler messageHandler;

    public static void register(MessageHandler messageHandler) {
        MessageProducer.messageHandler = messageHandler;
    }

    public static void produce(String rawMessage) {

        ObjectMapper mapper = new ObjectMapper();
        Message message = null;
        try {
            message = mapper.readValue(rawMessage, Message.class);

            if (message.getMessageType() == 2) {
                /**TEXT MESSAGE*/
                produceTextMessage(message);
            } else if (message.getMessageType() == 3) {
                /**INFORMATION MESSAGE */
                produceInformativeMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void produceInformativeMessage(Message message) {
        if (messageHandler != null) {
            messageHandler.informEvent(message);
        }

    }


    private static void produceTextMessage(Message message) {
        if (messageHandler != null) {
            messageHandler.handleText(message);
        }

    }
}
