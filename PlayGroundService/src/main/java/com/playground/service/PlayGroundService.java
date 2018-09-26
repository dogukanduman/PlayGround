package com.playground.service;

import com.playground.message.ClientInfo;
import com.playground.message.Message;

/**
 * Created by Dogukan Duman on 18.09.2018.
 */
public interface PlayGroundService {
    public void start();
    public void stop();
    public void sendMessageToEveryBody(ClientInfo clientInfo, Message message);


}
