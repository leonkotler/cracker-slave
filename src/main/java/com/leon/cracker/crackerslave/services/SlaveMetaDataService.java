package com.leon.cracker.crackerslave.services;

import com.leon.cracker.crackerslave.models.SlaveInfo;
import org.springframework.stereotype.Component;

@Component
public class SlaveMetaDataService implements ISlaveMetaDataService {

    private SlaveInfo slaveInfo;

    @Override
    public SlaveInfo getSlaveInfo() {
        return slaveInfo;
    }

    @Override
    public void setSlaveInfo(SlaveInfo slaveInfo) {
        this.slaveInfo = slaveInfo;
    }
}
