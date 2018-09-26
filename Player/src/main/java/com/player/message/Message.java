package com.player.message;


/**
 * Created by Dogukan Duman on 17.09.2018.
 */
public class Message {

    private String UUID;
    /** 0-> register
      * 1->unregister
      * 2->text message
      * 3->Information
      * */

    private int messageType;
    private String content;
    private ClientInfo clientInfo;

    public Message(int messageType, String content, ClientInfo clientInfo) {
        this.UUID = java.util.UUID.randomUUID().toString();
        this.messageType = messageType;
        this.content = content;
        this.clientInfo = clientInfo;
    }

    public Message() {
    }

    @Override
    public String toString() {
        return "Message{" +
                "UUID='" + UUID + '\'' +
                ", messageType=" + messageType +
                ", content='" + content + '\'' +
                ", clientInfo=" + clientInfo +
                '}';
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }
}
