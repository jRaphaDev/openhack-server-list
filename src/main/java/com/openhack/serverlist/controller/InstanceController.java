package com.openhack.serverlist.controller;

import com.openhack.serverlist.model.Instance;
import com.openhack.serverlist.service.InstanceService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class InstanceController {

    @Autowired
    InstanceService instanceService;

    @PostMapping("/instance")
    public String create() {
        instanceService.create();
        return "ok";
    }

    @GetMapping("/instance")
    public List<Instance> findAll() {
        return instanceService.findall();
    }

    @DeleteMapping("/instance/{instance}")
    public String delete(@PathVariable("instance") String pod) {
        instanceService.delete(pod);
        return "ok";
    }

}
