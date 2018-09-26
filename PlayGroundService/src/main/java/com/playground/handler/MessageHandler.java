package com.playground.handler;

import com.playground.message.ClientInfo;
import com.playground.message.Message;
import com.playground.service.MessageListener;

/**
 * Created by Dogukan Duman on 17.09.2018.
 */
public interface MessageHandler {

    /**
     * Register events which is produced by {@link MessageProducer}.
     * @param message
     * @param listener
     */
    void registerEvent(Message message, MessageListener listener);

    /**
     * UnRegister events which is produced  by {@link MessageProducer}.
     * @param clientInfo
     */
    void unRegisterEvent(ClientInfo clientInfo);

    /**
     * Text Message events which is produced by {@link MessageProducer}.
     * @param message
     */
    void handleText(Message message);
}
