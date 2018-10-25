package com.openhack.serverlist.service;

import com.openhack.serverlist.client.ClientAPI;
import com.openhack.serverlist.model.Instance;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class InstanceService {

    private final static String NAMESPACE = "default";

    @Autowired
    private ClientAPI client;

    private InstanceService() {}

    public Pod create(int x) {
        return client.createPod(NAMESPACE, x);
    }

    public List<Instance> findall() {
        PodList list = client.listPod(NAMESPACE);

        List<Instance> instances = new ArrayList<>();
        for(Pod pod: list.getItems()){
            Instance instance = Instance.builder()
                .name(pod.getMetadata().getName()).build();

            instances.add(instance);
        }

        return instances;
    }

    public boolean delete(String pod){
        return client.deletePod(NAMESPACE, pod);
    }


}
