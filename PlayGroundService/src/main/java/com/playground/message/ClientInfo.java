package com.playground.message;

/**
 * Created by Dogukan Duman on 17.09.2018.
 */
public class ClientInfo {


   private Integer portId;
   private String name;

    public Integer getPortId() {
        return portId;
    }

    public void setPortId(Integer portId) {
        this.portId = portId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientInfo(Integer portId, String name) {

        this.portId = portId;
        this.name = name;
    }
    public ClientInfo() {

    }

    @Override
    public String toString() {
        return "portId=" + portId + ", name=" + name;

    }
}
