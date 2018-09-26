package com.player;


import com.player.constant.Parameters;
import com.player.message.ClientInfo;
import com.player.service.PlayerService;

import java.util.logging.Logger;

/**
 * Created by Dogukan Duman on 17.09.2018.
 */
public class Application {


    private final static Logger LOGGER = Logger.getLogger(Application.class.getName());

    public static void test( PlayerService playerService, int howMany, int frequency,String message){
        try {
            playerService.start();
            Thread.sleep(1000);

            int counter=0;
            while((counter<howMany)&&(playerService.isAlive())){
                playerService.sendMessage(message);
                Thread.sleep(frequency*1000);
                counter++;
            }
          
            playerService.stop();
            
        } catch (Exception e) {
        }
    }


    /**
     * Player is simple client which communicate with other player via PlayerGroundService
     * Players can register to or unregister to PlayGroundService
     * Players can message to each other.
     *
     * @param args
     */



    public static void main(String args[]) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        int playerPort = Parameters.defaultPlayerPort;
        String playerName = Parameters.defaultPlayerName;
        int howMany =Parameters.defaultTestMessageNumber;
        int frequency =Parameters.defaultTestMessageFrequency;
        String message =Parameters.defaultTestMessage;

        if (args.length > 0) {
            playerPort = Integer.valueOf(args[0]);
        }
        if (args.length > 1) {
            playerName = args[1];
        }


        if (args.length > 2) {
            howMany = Integer.valueOf(args[2]);
        }
        /**  In seconds */
        if (args.length > 3) {
            frequency = Integer.valueOf(args[3]);
        }


        if (args.length > 4) {
            message = args[4];
        }


        /**Assign name and port to Player */
        ClientInfo clientInfo = new ClientInfo(playerPort, playerName);
        LOGGER.info("Player is created with" + clientInfo);

        PlayerService playerService = new PlayerService(clientInfo);

        test(playerService,howMany,frequency,message);

    }
}
