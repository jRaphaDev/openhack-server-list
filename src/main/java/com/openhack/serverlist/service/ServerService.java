package com.openhack.serverlist.service;

import com.openhack.serverlist.client.ClientAPI;

import org.springframework.beans.factory.annotation.Autowired;

public class ServerService {

    @Autowired
    private ClientAPI client;

    public void findall() {
        client.listServer();
    }
}
