package com.playground.service;

import com.playground.constant.Parameters;
import com.playground.handler.ExceptionHandler;
import com.playground.handler.MessageHandler;
import com.playground.handler.MessageProducer;
import com.playground.message.ClientInfo;
import com.playground.message.Message;
import com.playground.message.MessageBuilder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by Dogukan Duman on 17.09.2018.
 */
public class PlayGroundServiceImp implements PlayGroundService,MessageHandler,ExceptionHandler {

    private final static Logger LOGGER = Logger.getLogger(PlayGroundServiceImp.class.getName());

    /**
     * PlayGroundService port
     */

    private int port = 0;

    /**
     * How many seconds after last messaging occured for closing  PlayGroundService
     */
    private int timeout = 0;

    private String serviceName;

    /**
     * Socket parameters
     */
    private ServerSocket serverSocket = null;
    private Socket socket = null;

    /**
     * MessageSenderMap keeps MessageSender instances with port number
     * for every new registered client
     **/
    private Map<Integer, MessageSender> messageSenderMap = new ConcurrentHashMap<>();

    /**
     * MessageListenerMap keeps MessageListener instances with port number
     * for every new registered client
     **/
    private Map<Integer, MessageListener> messageListenerMap = new ConcurrentHashMap<>();


    /** Break flag for serverSocket.accept loop */
    private boolean isContinue = true;

    public PlayGroundServiceImp(int port, String serviceName, int timeout) {
        LOGGER.info("PlayGroundService is initializing");
        this.port = port;
        this.serviceName = serviceName;
        this.timeout = timeout;
        LOGGER.info("PlayGroundService is registering to MessageProducer");
        MessageProducer.register(this);
        LOGGER.info("PlayGroundService is initialized");
    }

    public void start() {

        startIdleController();
        try {
            /**Acquiring server socket*/
            serverSocket = new ServerSocket(port);

            while (isContinue) {

                /**Accepting new connections */
                socket = serverSocket.accept();
                LOGGER.info("New Client connection request received");
                new MessageListener(socket,this).start();
            }

        } catch (SocketTimeoutException e) {
            LOGGER.info("PlayGroundService is so alone. it will close ");
        } catch (IOException e) {
           // LOGGER.info("I/O error: " + e);
        } finally {
            stop();
        }
    }

    /**
     * Stop PlayGround Service gracefully
     */
    public void stop() {

        if (isContinue) {
            try {
                LOGGER.info("PlayGroundService will be closed gracefully.");
                /**Break the while loop*/
                isContinue = false;

                /**Closing MessageListener and MessageSender instances */
                closeMessageInstances();

                /**Close server socket */
                serverSocket.close();

                /**Close socket, null check, socket may be not initialized */
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                //LOGGER.info("I/O error: " + e);
            }
        }


    }
    /**
     * Inform everybody about somebody is added to playground service.
     */
    public void sendMessageToEveryBody(ClientInfo clientInfo, Message message) {
        messageSenderMap.entrySet().stream().forEach(x -> {
            if (!x.getValue().getClientInfo().getPortId().equals(clientInfo.getPortId())) {
                x.getValue().sendMessage(message);
                LOGGER.info(" Message is sent to " + x.getValue().getClientInfo().getName() + " by " + clientInfo.getName());
            }
        });
    }

    /**
     * MessageHandler METHODS
     */
    @Override
    public void registerEvent(Message message, MessageListener listener) {


        /**MessageListener added to messageListenerMap for closing connection later */
        LOGGER.info("New MessageListener is added to messageListenerMap for client:" + listener.getClientInfo());
        messageListenerMap.put(message.getClientInfo().getPortId(), listener);


        /**A MessageSender is create for communication later with related client*/
        MessageSender messageSender = new MessageSender(message.getClientInfo(),this);


        /**MessageSender added to messageSenderMap for closing connection or sending message later */
        LOGGER.info("New MessageSender is added to messageSenderMap :" + listener.getClientInfo());
        messageSenderMap.put(message.getClientInfo().getPortId(), messageSender);

        LOGGER.info("Registration is finished successfully for client:" + message.getClientInfo());

        /**Informing to client*/
        LOGGER.info("Client will be informed about successfully registration");
        messageSender.sendMessage(MessageBuilder.registrationSucceed(messageSender.getClientInfo(), new ClientInfo(port, serviceName)));

        /**Informing to everybody */
        LOGGER.info("Every registered client will be informed about client:" + message.getClientInfo());
        sendMessageToEveryBody(message.getClientInfo(), MessageBuilder.registrationInform(message.getClientInfo(), new ClientInfo(port, serviceName)));


    }
    @Override
    public void unRegisterEvent(ClientInfo clientInfo) {

        LOGGER.info("UnRegisterEvent came from client:" + clientInfo);

        MessageSender ms = messageSenderMap.get(clientInfo.getPortId());
        ms.closeConnection();
        MessageListener ml = messageListenerMap.get(clientInfo.getPortId());
        ml.closeConnection();
        LOGGER.info("Client:" + clientInfo + " is unregistered");
        sendMessageToEveryBody(clientInfo, MessageBuilder.unRegistrationSucceed(clientInfo, new ClientInfo(port, serviceName)));
    }


    @Override
    public void handleText(Message message) {
        ClientInfo clientInfo = message.getClientInfo();
        LOGGER.info("##"+clientInfo.getName() + "##:" + message.getContent());
        sendMessageToEveryBody(message.getClientInfo(), message);
    }

    /**
     * ExceptionHandler METHODS
     */
    @Override
    public void handleError(ClientInfo clientInfo, Exception e) {


        MessageSender ms = messageSenderMap.get(clientInfo.getPortId());
        if(ms!=null){
            ms.closeConnection();
            messageSenderMap.remove(clientInfo.getPortId());
        }

        MessageListener ml = messageListenerMap.get(clientInfo.getPortId());
        if(ml!=null){
            ml.closeConnection();
            messageListenerMap.remove(clientInfo.getPortId());
        }

        LOGGER.info("Client:" + clientInfo + " is unregistered");
        sendMessageToEveryBody(clientInfo, MessageBuilder.unRegistrationSucceed(clientInfo, new ClientInfo(port, serviceName)));
    }





    /**
     * PRIVATE METHODS
     */

    /**Closing MessageListener and MessageSender instanes */
    private void closeMessageInstances() {

        messageSenderMap.entrySet().stream().forEach(x -> {
            LOGGER.info("Message sender for " + x.getValue().getClientInfo() + " is deleting.");
            x.getValue().closeConnection();
        });

        messageListenerMap.entrySet().stream().forEach(x -> {
            LOGGER.info("Message Listener for " + x.getValue().getClientInfo() + " is deleting.");
            x.getValue().closeConnection();
        });

        messageSenderMap = new ConcurrentHashMap<>();
        messageListenerMap = new ConcurrentHashMap<>();

    }

    /**
     * StartIdleController starts PlayGroundService
     * closing flow, if there is no message in last n seconds.
     * {@link Parameters#defaultPlayGroundIdleTimeOut} or {@link PlayGroundServiceImp#timeout}
     * Checks in every {@link Parameters#defaultPlayGroundIdleCheckTimeOut} seconds
     */
   private void startIdleController(){

       Runnable isServiceIdleController = () -> {
           try {
               while (true) {
                   Duration duration = Duration.between(MessageProducer.getLastMessagingDate(), Instant.now());
                   LOGGER.info("Last message update:"+ MessageProducer.getLastMessagingDate());
                   if (duration.getSeconds() > timeout) {
                       LOGGER.info("PlayGroundService is so alone. it will close ");
                       serverSocket.close();
                       break;
                   }
                   Thread.sleep(Parameters.defaultPlayGroundIdleCheckTimeOut*1000);
               }
           } catch (InterruptedException e) {
               LOGGER.info(e.getMessage());
           } catch (SocketException e) {
               LOGGER.info(e.getMessage());
           } catch (IOException e) {
               e.printStackTrace();
           }
       };
       new Thread(isServiceIdleController).start();
   }


}


