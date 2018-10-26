package com.openhack.serverlist.controller;

import com.openhack.serverlist.model.Instance;
import com.openhack.serverlist.model.InstanceReplica;
import com.openhack.serverlist.service.InstanceService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class InstanceController {

    @Autowired
    InstanceService instanceService;

    @PostMapping("/instance")
    public String create(@RequestBody() InstanceReplica instanceReplica) {
        System.out.println("testing");
        instanceService.create(instanceReplica.getNumber());
        return "ok";
    }

    @GetMapping("/instance")
    public List<Instance> findAll() {
        return instanceService.findall();
    }

    @DeleteMapping("/instance")
    public String delete() {
        instanceService.delete();
        return "ok";
    }

}
