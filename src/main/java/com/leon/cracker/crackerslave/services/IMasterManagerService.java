package com.leon.cracker.crackerslave.services;

import com.leon.cracker.crackerslave.models.FoundPasswordRequest;
import com.leon.cracker.crackerslave.models.RegisterWithMasterRequest;
import com.leon.cracker.crackerslave.models.SlaveCrackingRequest;

import java.net.URI;

public interface IMasterManagerService {

    boolean isSlaveIsRegistered();

    void setSlaveIsRegistered(boolean slaveIsRegistered);

    String getMasterHost();
    void setMasterHost(String host);

    int getMasterPort();
    void setMasterPort(int port);

    void registerWithMaster(RegisterWithMasterRequest registerRequest);

    boolean isMasterUp();

    void notifyFoundPassword(FoundPasswordRequest foundPasswordRequest);

    void notifySlaveIsDone(SlaveCrackingRequest slaveCrackingRequest);

    URI getMasterURI();
}
