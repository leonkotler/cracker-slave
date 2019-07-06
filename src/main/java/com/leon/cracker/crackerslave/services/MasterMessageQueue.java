package com.leon.cracker.crackerslave.services;

import com.leon.cracker.crackerslave.externalapi.IMasterApi;
import com.leon.cracker.crackerslave.models.FoundPasswordRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class MasterMessageQueue implements IMasterMessageQueue {

    private static final Logger logger = LoggerFactory.getLogger(MasterMessageQueue.class);

    @Value("${master.messaging-queue.capacity}")
    private int queueSize;

    @Value("${master.retry-request.timeout}")
    private int retryTimeout;

    private IMasterApi masterApi;
    private IMasterManagerService masterManagerService;
    private BlockingQueue<FoundPasswordRequest> requestQueue;


    @Autowired
    public void setMasterApi(IMasterApi masterApi) {
        this.masterApi = masterApi;
    }

    @Autowired
    public void setMasterManagerService(IMasterManagerService masterManagerService) {
        this.masterManagerService = masterManagerService;
    }

    @Override
    public void addFoundPasswordRequest(FoundPasswordRequest foundPasswordRequest) {
        try {
            requestQueue.put(foundPasswordRequest);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FoundPasswordRequest pollFoundPasswordRequest() {
        try {
            return requestQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostConstruct
    public void postConstruct() {
        requestQueue = new ArrayBlockingQueue<>(queueSize);

        new Thread(() -> {
            while (true) {
                FoundPasswordRequest foundPasswordRequest = pollFoundPasswordRequest();

                if (masterManagerService.isMasterUp() && masterManagerService.isSlaveIsRegistered()) {
                    masterApi.foundPassword(masterManagerService.getMasterURI(), foundPasswordRequest);
                } else {
                    logger.error("Master is down, cannot send FoundPasswordRequest, retrying...");
                    retryIndefinitelyUntilMasterIsUp(foundPasswordRequest);
                }
            }
        }).start();


    }

    private void retryIndefinitelyUntilMasterIsUp(FoundPasswordRequest foundPasswordRequest) {
        do {
            try {

                logger.info("Waiting for {}ms before retrying", retryTimeout);
                Thread.sleep(retryTimeout);
                masterApi.foundPassword(masterManagerService.getMasterURI(), foundPasswordRequest);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!masterManagerService.isMasterUp() && !masterManagerService.isSlaveIsRegistered());
    }


}
