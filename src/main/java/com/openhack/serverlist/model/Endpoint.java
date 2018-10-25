package com.openhack.serverlist.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Endpoint {
    private String minecraft;
    private String rcon;
}
