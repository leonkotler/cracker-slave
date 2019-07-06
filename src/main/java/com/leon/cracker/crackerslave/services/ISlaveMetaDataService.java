package com.leon.cracker.crackerslave.services;

import com.leon.cracker.crackerslave.models.SlaveCrackingRequest;
import com.leon.cracker.crackerslave.models.SlaveInfo;

import java.util.List;

public interface ISlaveMetaDataService {

    void setSlaveInfo(SlaveInfo slaveInfo);
    SlaveInfo getSlaveInfo();
    void addRequest(SlaveCrackingRequest slaveCrackingRequest);
    void removeRequest(SlaveCrackingRequest slaveCrackingRequest);
    List<SlaveCrackingRequest> getCurrentRequests();
}
