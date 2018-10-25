package com.openhack.serverlist.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;


public class Server {

    private String name;
    private Map<String, String> endpoints;

    public Server(String serverName) {
        name = serverName;
        endpoints = new HashMap<String, String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Map<String, String> getEndpoints() {
        return endpoints;
    }


    public void setEndpoints(Map<String, String> endpoints) {
        this.endpoints = endpoints;
    }

    public void addEndpoint(String name, String ipPort) {
        this.endpoints.put(name, ipPort);
    }


}
