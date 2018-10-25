package com.openhack.serverlist.service;

import com.openhack.serverlist.client.ClientAPI;

import io.fabric8.kubernetes.api.model.ServiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerService {

    @Autowired
    private ClientAPI client;

    public void findall() {
        ServiceList list = client.listServer("default");
    }
}
