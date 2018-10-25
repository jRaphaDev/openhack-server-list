package com.openhack.serverlist.controller;

import com.openhack.serverlist.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {

    @Autowired
    ServerService serverService;

    @GetMapping("/server")
    public String findAll() {
        serverService.findall();
        return "listou";
    }
}
