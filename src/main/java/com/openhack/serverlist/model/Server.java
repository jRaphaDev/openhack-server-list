package com.openhack.serverlist.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Server {
    private String name;
    private Endpoint endpoint;
}
