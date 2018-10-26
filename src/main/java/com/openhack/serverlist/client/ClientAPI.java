package com.openhack.serverlist.client;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetBuilder;
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
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
                	.withName(serviceName)
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

    public StatefulSet createStatefulSet(String namespace, String serviceName) {
    	
    	
    	StatefulSet ss1 = new StatefulSetBuilder()
    			.withNewMetadata()
    				.withName(serviceName)
    			.endMetadata()
    			.withNewSpec()
    				.withServiceName(serviceName)
    				.withReplicas(1)
    				.withNewSelector()
    					.withMatchLabels(Collections.singletonMap("pod-name", serviceName))
    				.endSelector()
    				.withNewTemplate()
    					.withNewMetadata()
    						.addToLabels("pod-name", serviceName)
    					.endMetadata()
    					.withNewSpec()
    						.withHostname(serviceName)
    						.addNewContainer()
    							.withName(serviceName)
    							.withImage("openhack/minecraft-server:2.0")
    							.addNewVolumeMount()
    								.withMountPath("/data")
    								.withName("mcdata")
    							.endVolumeMount()
    							.addNewPort()
    								.withContainerPort(25565)
    								.withContainerPort(25575)
    							.endPort()
    							.addNewEnv()
    								.withName("EULA")
    								.withValue("TRUE")
    							.endEnv()
    						.endContainer()
    					.endSpec()
    				.endTemplate()
    				.addNewVolumeClaimTemplate()
    					.withNewMetadata()
    						.withName("mcdata")
    					.endMetadata()
    					.withNewSpec()
    						.withAccessModes("ReadWriteOnce")
    						.withStorageClassName("scminecraft")
    						.withNewResources()
    							.addToRequests("storage", new Quantity("5Gi"))
    						.endResources()
    					.endSpec()
    				.endVolumeClaimTemplate()
    			.endSpec()
    			.build();
    	
    	return client.apps().statefulSets().inNamespace(namespace).create(ss1);
    }

    public boolean deleteStatefulSet(String namespace, String statefulSetName) {
    	return client.apps().statefulSets().inNamespace(namespace).withName(statefulSetName).delete();
    }
    
    public boolean deletePersistentVolumeClaims(String namespace, String statefulSetName) {
    	return client.persistentVolumeClaims().inNamespace(namespace).withLabel("volume-pod", statefulSetName).delete();
    }
}
