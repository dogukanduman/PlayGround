package com.playground;

import com.playground.constant.Parameters;
import com.playground.service.PlayGroundService;
import com.playground.service.PlayGroundServiceImp;

import java.util.logging.Logger;


/**
 * Created by Dogukan Duman on 17.09.2018.
 */
public class Application {


    private final static Logger LOGGER = Logger.getLogger(Application.class.getName());


    /**
     * PlayGround is responsible for registering and messaging between players
     * Players can register to or unregister from PlayGroundService
     * Players can message to each other.
     * @param args
     */

    public static void main(String args[])
    {

        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        int playGroundPort = Parameters.defaultPlayGroundPort;

        if(args.length>0){
            playGroundPort = Integer.valueOf(args[0]);
        }

        LOGGER.info("PlayGround is starting with port number :"+playGroundPort);
        PlayGroundService playGroundService  = new PlayGroundServiceImp(playGroundPort,"PlayGroundService", Parameters.defaultPlayGroundIdleTimeOut);
        LOGGER.info("#############################");
        LOGGER.info("Welcome to PlayGroundService");
        LOGGER.info("PlayGround is responsible for registering and messaging between players");
        playGroundService.start();
        playGroundService.stop();

    }
}
