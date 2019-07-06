package com.leon.cracker.crackerslave.services;

import com.leon.cracker.crackerslave.externalapi.IMasterApi;
import com.leon.cracker.crackerslave.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class MasterManager implements IMasterManagerService {


    private String host;
    private int port;
    private boolean slaveIsRegistered;
    private IMasterApi masterApi;
    private ISlaveMetaDataService slaveMetaDataService;
    private IMasterMessageQueue masterMessageQueue;
    private static final Logger logger = LoggerFactory.getLogger(MasterManager.class);

    @Autowired
    public void setMasterApi(IMasterApi masterApi) {
        this.masterApi = masterApi;
    }

    @Autowired
    public void setSlaveMetaDataService(ISlaveMetaDataService slaveMetaDataService) {
        this.slaveMetaDataService = slaveMetaDataService;
    }

    @Autowired
    public void setMasterMessageQueue(IMasterMessageQueue masterMessageQueue) {
        this.masterMessageQueue = masterMessageQueue;
    }

    @Override
    public boolean isSlaveIsRegistered() {
        return slaveIsRegistered;
    }

    @Override
    public void setSlaveIsRegistered(boolean slaveIsRegistered) {
        this.slaveIsRegistered = slaveIsRegistered;
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
    public void registerWithMaster(RegisterWithMasterRequest registerRequest) {
        RestTemplate restTemplate = new RestTemplate();
        URI registerSlaveUri = URI.create("http://" + host + ":" + port + "/master/register-slave");

        try {
            restTemplate.postForEntity(registerSlaveUri, registerRequest, RegisterWithMasterRequest.class);
            logger.info("Registered successfully with master");
            slaveIsRegistered = true;
        } catch (ResourceAccessException exception){
            logger.error("Couldn't register with master, it is probably down. Exception is:\n {}", exception.getMessage());
        }
    }

    @Override
    public boolean isMasterUp() {
        return masterApi.isMasterUp(getMasterURI());
    }

    @Override
    public void notifyFoundPassword(FoundPasswordRequest foundPasswordRequest) {
        masterMessageQueue.addFoundPasswordRequest(foundPasswordRequest);
    }

    @Override
    public void notifySlaveIsDone(SlaveCrackingRequest slaveCrackingRequest) {
        slaveMetaDataService.removeRequest(slaveCrackingRequest);
        masterApi.slaveIsDone(getMasterURI(), new SlaveDoneRequest(slaveMetaDataService.getSlaveInfo(), slaveCrackingRequest));
    }

    @Override
    public URI getMasterURI() {
        return URI.create("http://" + host + ":" + port);
    }
}
