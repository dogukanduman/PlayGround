package com.player.service;


import com.player.constant.Parameters;
import com.player.handler.ExceptionHandler;
import com.player.handler.MessageHandler;
import com.player.handler.MessageProducer;
import com.player.message.ClientInfo;
import com.player.message.Message;
import com.player.message.MessageBuilder;

import java.util.logging.Logger;

/**
 * Created by Dogukan Duman on 18.09.2018.
 */
public class PlayerService  implements MessageHandler,ExceptionHandler {

    private final static Logger LOGGER = Logger.getLogger(PlayerService.class.getName());
    private ClientInfo clientInfo;
    private MessageSender messageSender;
    private MessageListener messageListener;
    private boolean alive = false;
    public PlayerService(ClientInfo clientInfo){

        this.clientInfo = clientInfo;
        LOGGER.info("PlayerService is registering to MessageProducer");
        MessageProducer.register(this);

    }

    public void start() throws Exception{


        /**Listen for incoming message from PlayerGroundService */
        messageListener = new MessageListener( this.clientInfo.getPortId(),this);
        messageListener.start();


        /**Make connection with PlayerGroundService for */
        ClientInfo server =new ClientInfo(Parameters. defaultPlayGroundPort,"Playground");
        messageSender =new MessageSender(server,this);

        /**Send registration message to playground*/
        LOGGER.info("Registration message is sending to playground" );
        messageSender.sendMessage(MessageBuilder.registerMe(clientInfo));

        alive = true;
    }
     public void stop(){
         LOGGER.info("PlayerService will be close gracefully.");
         messageListener.closeConnection();
         messageSender.closeConnection();
         messageSender.sendMessage(MessageBuilder.unRegisterMe(clientInfo));

     }

     /** Sending Message to all clients*/
     public void sendMessage(String message){
         LOGGER.info("##ME##:"+message);
         messageSender.sendMessage(MessageBuilder.textMessage(clientInfo,message));
     }

    public void informEvent(Message message) {
        LOGGER.info("##PlayGroundServic##e:"+message.getContent() );
    }

    public void handleText(Message message) {
         ClientInfo clientInfo = message.getClientInfo();
        LOGGER.info(clientInfo.getName()+":"+message.getContent());

    }

    public void handle(Exception e) {

        LOGGER.info( "Could not connect to PlayGroundService,First start PlayGroundService");

        if(messageSender!=null){messageSender.closeConnection();}

        if(messageListener!=null){ messageListener.closeConnection();}

        alive = false;
        //throw new Exception("Could not connect to PlayGroundService,First start PlayGroundService");
    }

    public boolean isAlive() {
        return alive;
    }
}
