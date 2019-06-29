package com.leon.cracker.crackerslave.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class MasterManager implements MasterManagerService {


    private String host;
    private int port;



    @Override
    public String getMasterHost() {
        return host;
    }

    @Override
    public void setMasterHost(String host) {
        this.host = host;
    }

    @Override
    public int getMasterPort() {
        return port;
    }

    @Override
    public void setMasterPort(int port) {
        this.port = port;
    }

    @Override
    public void registerWithMaster(SlaveInfo slaveInfo) {
        RestTemplate restTemplate = new RestTemplate();
        URI registerSlaveUri = URI.create("http://" + host + ":" + port + "/master/register-slave");
        restTemplate.postForEntity(registerSlaveUri, slaveInfo, SlaveInfo.class);
    }
}
