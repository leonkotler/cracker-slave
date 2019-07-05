package com.leon.cracker.crackerslave.services;

import com.leon.cracker.crackerslave.models.SlaveInfo;

public interface ISlaveMetaDataService {

    void setSlaveInfo(SlaveInfo slaveInfo);
    SlaveInfo getSlaveInfo();
}
