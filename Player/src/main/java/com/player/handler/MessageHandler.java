package com.player.handler;

import com.player.message.Message;

/**
 * Created by Dogukan Duman on 17.09.2018.
 */
public interface MessageHandler {



    /**
     * Informative Message events which is produced  by {@link MessageProducer}.
     * @param message
     */
    void informEvent(Message message);

    /**
     * Text Message events which is produced by {@link MessageProducer}.
     * @param message
     */
    void handleText(Message message);
}
