package com.openhack.serverlist.client;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.storage.StorageClassList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.PodResource;
import io.kubernetes.client.proto.V1Storage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

        Integer total = client.apps()
                .statefulSets().inNamespace(namespace)
                .withName("minecraft").get()
                .getSpec().getReplicas();

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

    public io.fabric8.kubernetes.api.model.Service createService(String namespace, String serviceName) {

        io.fabric8.kubernetes.api.model.Service service = client.services().inNamespace(namespace).createNew()
                .withNewMetadata()
                .withGenerateName(serviceName)
                .endMetadata()
                .withNewSpec()
                .addNewPort()
                .withProtocol("TCP")
                .withPort(80)
                .withName("tcp80")
                .withNewTargetPort(25565)
                .endPort()
                .addNewPort()
                .withProtocol("TCP")
                .withPort(443)
                .withName("ssh")
                .withNewTargetPort(25575)
                .endPort()
                .addNewPort()
                .withProtocol("TCP")
                .withPort(25565)
                .withName("http")
                .withNewTargetPort(25565)
                .endPort()
                .addNewPort()
                .withProtocol("TCP")
                .withPort(25575)
                .withName("https")
                .withNewTargetPort(25575)
                .endPort()
                .addToSelector("pod-name", serviceName)
                .withType("LoadBalancer")
                .withSessionAffinity("ClientIP")
                .endSpec()
                .done();

        return service;

    }

    public Boolean deleteService(String namespace, String serviceName){

        return client.services().inNamespace(namespace).withName(serviceName).delete();

    }

    public io.fabric8.kubernetes.api.model.Service getService(String namespace, String serviceName) {
        return client.services().inNamespace(namespace).withName(serviceName).get();

    }

    public void createPodService(String namespace, String serviceName) {

        String fileName = "/resource/podopenhack";

        File file = new File(fileName);
        if (!file.exists() || !file.isFile()) {
            System.err.println("File does not exist: " + fileName);
            return;
        }

        List<HasMetadata> resources;
        try {
            resources = client.load(new FileInputStream(fileName)).get();

            if (resources.isEmpty()) {
                System.err.println("No resources loaded from file: " +fileName);
                return;
            }

            HasMetadata resource = resources.get(0);

            if (resource instanceof Pod) {
                Pod pod = (Pod) resource;
                System.out.println("Creating pod in namespace " + namespace);
                NonNamespaceOperation<Pod, PodList, DoneablePod, PodResource<Pod, DoneablePod>> pods = client.pods().inNamespace(namespace);

                Pod result = pods.create(pod);
                System.out.println("Created pod " + result.getMetadata().getName());
                return;
            } else {
                System.err.println("Loaded resource is not a Pod! " + resource);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
