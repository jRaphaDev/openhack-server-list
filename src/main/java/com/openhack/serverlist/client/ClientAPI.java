package com.openhack.serverlist.client;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.storage.StorageClassList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import io.kubernetes.client.proto.V1Storage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClientAPI {

    private KubernetesClient client;

    private ClientAPI() {
        client = new DefaultKubernetesClient();
    }

    public Pod createPod(String namespace, Integer n) {

        Integer total = client.apps().statefulSets().inNamespace(namespace).withName("minecraft").get().getSpec().getReplicas();

        client.apps().statefulSets()
                .inNamespace(namespace)
                .withName("minecraft").scale((total + n), true);

        return null;
    }

    public PodList listPod(String namespace) {
        PodList list = client.pods().inNamespace(namespace).list();
        for (Pod item : list.getItems()) {
            System.out.println(item.getMetadata().getName());
        }
        return list;
    }

    public boolean deletePod(String namespace, String pod){
        return client.pods().inNamespace(namespace)
                .withName(pod).delete();
    }

    public ServiceList listServer(String namespace) {
        ServiceList list = client.services().inNamespace(namespace).list();
        for (io.fabric8.kubernetes.api.model.Service service: list.getItems()) {
            System.out.println(service.getMetadata().getName());
        }
        return list;
    }

}
