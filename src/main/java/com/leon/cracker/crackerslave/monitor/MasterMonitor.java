package com.leon.cracker.crackerslave.monitor;

import com.leon.cracker.crackerslave.models.RegisterWithMasterRequest;
import com.leon.cracker.crackerslave.services.ISlaveMetaDataService;
import com.leon.cracker.crackerslave.services.MasterManager;
import com.leon.cracker.crackerslave.services.IMasterManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MasterMonitor {

    private IMasterManagerService masterManagerService;
    private ISlaveMetaDataService slaveMetaDataService;

    @Value("${master.retry-request.timeout}")
    private int retryTimeout;

    private static final Logger logger = LoggerFactory.getLogger(MasterManager.class);

    @Autowired
    public void setMasterManagerService(IMasterManagerService masterManagerService) {
        this.masterManagerService = masterManagerService;
    }

    @Autowired
    public void setSlaveMetaDataService(ISlaveMetaDataService slaveMetaDataService) {
        this.slaveMetaDataService = slaveMetaDataService;
    }

    @PostConstruct
    public void monitorMasterStatus() {

        new Thread(() -> {
            while (true) checkIfMasterIsUp();
        }).start();
    }

    private void checkIfMasterIsUp() {
        try {

            Thread.sleep(retryTimeout);

            if (masterManagerService.isMasterUp())
                logger.info("Master is UP!");
            else {
                waitUntilMasterIsUpAndReRegister();

                // we register one more time to solve race-condition
                masterManagerService.registerWithMaster(
                        new RegisterWithMasterRequest(
                                slaveMetaDataService.getSlaveInfo(),
                                slaveMetaDataService.getCurrentRequests()
                        ));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitUntilMasterIsUpAndReRegister() {
        do {
            try {
                logger.info("Master is DOWN! Trying to re-register each {}ms", retryTimeout);
                Thread.sleep(retryTimeout);

                masterManagerService.setSlaveIsRegistered(false);
                masterManagerService.registerWithMaster(
                        new RegisterWithMasterRequest(
                                slaveMetaDataService.getSlaveInfo(),
                                slaveMetaDataService.getCurrentRequests()
                        ));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (!masterManagerService.isMasterUp());

    }
}
