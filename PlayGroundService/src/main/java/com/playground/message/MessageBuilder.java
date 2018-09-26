package com.playground.message;

/**
 * Created by Dogukan Duman on 18.09.2018.
 */
public class MessageBuilder {

    public static Message registrationSucceed(ClientInfo info, ClientInfo owner) {
        return new Message(3, "Hi " + info.getName() + ", I added you", new ClientInfo(owner.getPortId(), owner.getName()));
    }

    public static Message unRegistrationSucceed(ClientInfo info, ClientInfo owner) {
        return new Message(3, "Hi everybody!, I removed client " + info.getName() + " Say Goodby to Him", owner);
    }

    public static Message registrationInform(ClientInfo info, ClientInfo owner) {
        return new Message(3, "Hi everybody!, I added client " + info.getName() + " Say Hi to Him", owner);
    }


}
