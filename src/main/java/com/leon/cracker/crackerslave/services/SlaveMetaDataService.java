package com.leon.cracker.crackerslave.services;

import com.leon.cracker.crackerslave.models.SlaveCrackingRequest;
import com.leon.cracker.crackerslave.models.SlaveInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SlaveMetaDataService implements ISlaveMetaDataService {

    private SlaveInfo slaveInfo;
    private List<SlaveCrackingRequest> currentRequests = new ArrayList<>();

    @Override
    public SlaveInfo getSlaveInfo() {
        return slaveInfo;
    }

    @Override
    public void addRequest(SlaveCrackingRequest slaveCrackingRequest) {
        currentRequests.add(slaveCrackingRequest);
    }

    @Override
    public void setSlaveInfo(SlaveInfo slaveInfo) {
        this.slaveInfo = slaveInfo;
    }

    @Override
    public void removeRequest(SlaveCrackingRequest slaveCrackingRequest) {
        currentRequests.remove(slaveCrackingRequest);
    }

    @Override
    public List<SlaveCrackingRequest> getCurrentRequests() {
        return currentRequests;
    }


}
