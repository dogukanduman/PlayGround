package com.playground.handler;

import com.playground.message.ClientInfo;


/**
 * Created by Dogukan Duman on 18.09.2018.
 */
public interface ExceptionHandler {

    void handleError(ClientInfo clientInfo, Exception e);

}
