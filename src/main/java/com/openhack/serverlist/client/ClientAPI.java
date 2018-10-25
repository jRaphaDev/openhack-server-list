package com.openhack.serverlist.client;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import org.springframework.stereotype.Service;

@Service
public class ClientAPI {

    private KubernetesClient client;

    private ClientAPI() {
        client = new DefaultKubernetesClient();
    }

    public Pod createPod(String namespace, String podName) {
        Pod pod = client.pods().inNamespace(namespace)
                .create(new PodBuilder().withNewMetadata().withName(podName).endMetadata().build());
        return pod;
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
