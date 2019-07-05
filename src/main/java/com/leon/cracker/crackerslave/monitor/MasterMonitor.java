package com.leon.cracker.crackerslave.monitor;

import com.leon.cracker.crackerslave.services.ISlaveMetaDataService;
import com.leon.cracker.crackerslave.services.MasterManager;
import com.leon.cracker.crackerslave.services.IMasterManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MasterMonitor {

    private IMasterManagerService masterManagerService;
    private ISlaveMetaDataService slaveMetaDataService;
    private static final Logger logger = LoggerFactory.getLogger(MasterManager.class);

    @Autowired
    public void setMasterManagerService(IMasterManagerService masterManagerService) {
        this.masterManagerService = masterManagerService;
    }

    @Autowired
    public void setSlaveMetaDataService(ISlaveMetaDataService slaveMetaDataService) {
        this.slaveMetaDataService = slaveMetaDataService;
    }

    @Scheduled(fixedRate = 10000)
    public void checkMaster(){

        if (masterManagerService.isMasterUp())
            logger.info("Master is UP!");
        else {
            logger.info("Master is DOWN! Trying to re-register");
            masterManagerService.registerWithMaster(slaveMetaDataService.getSlaveInfo());
        }
    }
}
