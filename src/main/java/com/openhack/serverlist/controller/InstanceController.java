package com.openhack.serverlist.controller;

import com.openhack.serverlist.service.InstanceService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

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
    public String findAll() {
        instanceService.findall();
        return "ok";
    }

    @DeleteMapping("/instance/{instance}")
    public String delete(@PathVariable("instance") String pod) {
        instanceService.delete(pod);
        return "ok";
    }

}
