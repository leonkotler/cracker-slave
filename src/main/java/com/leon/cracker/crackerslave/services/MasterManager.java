package com.leon.cracker.crackerslave.services;

import com.leon.cracker.crackerslave.externalapi.IMasterApi;
import com.leon.cracker.crackerslave.models.FoundPasswordRequest;
import com.leon.cracker.crackerslave.models.HealthStatus;
import com.leon.cracker.crackerslave.models.SlaveInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class MasterManager implements IMasterManagerService {


    private String host;
    private int port;
    private IMasterApi masterApi;
    private static final Logger logger = LoggerFactory.getLogger(MasterManager.class);

    @Autowired
    public void setMasterApi(IMasterApi masterApi) {
        this.masterApi = masterApi;
    }

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

        try {
            restTemplate.postForEntity(registerSlaveUri, slaveInfo, SlaveInfo.class);
            logger.info("Registered successfully with master");
        } catch (ResourceAccessException exception){
            logger.error("Couldn't register with master, it is probably down. Exception is:\n {}", exception.getMessage());
        }
    }

    @Override
    public boolean isMasterUp() {
        return masterApi.isMasterUp(getMasterURI());
    }

    @Override
    public void notifyFoundPassword(String requestId, String hash, String password) {
        masterApi.foundPassword(getMasterURI(), new FoundPasswordRequest(requestId, hash, password));
    }

    private URI getMasterURI() {
        return URI.create("http://" + host + ":" + port);
    }
}
