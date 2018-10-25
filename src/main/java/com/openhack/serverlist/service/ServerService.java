package com.openhack.serverlist.service;

import com.openhack.serverlist.client.ClientAPI;

import com.openhack.serverlist.model.Server;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.ServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ServerService {

    @Autowired
    private ClientAPI client;

    private final static String NAMESPACE = "default";


    public List<Server> findall() {


        List<Server> listServers = new ArrayList<>();

        ServiceList serviceList = client.listServer(NAMESPACE);


        for (io.fabric8.kubernetes.api.model.Service service : serviceList.getItems()) {

            Server server = new Server(service.getMetadata().getName());

            if ( service.getSpec().getType().compareTo("LoadBalancer") == 0) {
                for (ServicePort porta : service.getSpec().getPorts()) {

                    if (porta.getName().compareTo("http") == 0) {
                        server.addEndpoint("minecraft", service.getStatus().getLoadBalancer().getIngress().get(0).getIp() + ":" + porta.getPort());
                    }

                    if (porta.getName().compareTo("https") == 0) {
                        server.addEndpoint("rcon", service.getStatus().getLoadBalancer().getIngress().get(0).getIp() + ":" + porta.getPort());
                    }
                }
                listServers.add(server);
            }

        }

        return listServers;

    }

    public void createService() {

        UUID uuid = UUID.randomUUID();

        String serviceName = "minecraft";
        serviceName.concat(uuid.toString());


        client.createService(NAMESPACE, serviceName);
    }

    public boolean deleteService(String serverName) {
        return client.deleteService(NAMESPACE, serverName);
    }
}
