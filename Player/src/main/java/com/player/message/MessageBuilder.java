package com.player.message;

/**
 * Created by Dogukan Duman on 18.09.2018.
 */
public class MessageBuilder {



    public static Message registerMe(ClientInfo clientInfo){
        return new Message( 0,"I am "+clientInfo.getName()+", Can you add me",clientInfo);
    }

    public static Message unRegisterMe(ClientInfo clientInfo){
        return new Message(  1,"I am "+clientInfo.getName()+", Can you delete me",clientInfo);
    }

    public static Message textMessage(ClientInfo clientInfo, String content){
        return new Message(  2,content,clientInfo);
    }
}
