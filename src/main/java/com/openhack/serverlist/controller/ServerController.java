package com.openhack.serverlist.controller;

import com.openhack.serverlist.model.Server;
import com.openhack.serverlist.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
public class ServerController {

    @Autowired
    ServerService serverService;

    @GetMapping("/server")
    public Iterable<Server> findAll() {
        Iterable<Server> listServers = serverService.findall();
        return listServers;
    }

    @PostMapping("/server")
    public String createServer() {
        serverService.createService();
        return "ok";
    }

    @DeleteMapping("/server/{serverName}")
    public String deleteServer(@PathVariable String serverName) {
        serverService.deleteService(serverName);
        return "ok";
    }
}
