package com.openhack.serverlist.service;

import com.openhack.serverlist.client.ClientAPI;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InstanceService {

    private final static String NAMESPACE = "default";

    @Autowired
    private ClientAPI client;

    private InstanceService() {}

    public Pod create() {
        return client.createPod(NAMESPACE, NAMESPACE);
    }

    public PodList findall() {
        return client.listPod(NAMESPACE);
    }

    public boolean delete(String pod){
        return client.deletePod(NAMESPACE, pod);
    }


}
