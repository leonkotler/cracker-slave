package com.leon.cracker.crackerslave.services;

import com.leon.cracker.crackerslave.models.SlaveInfo;

public interface IMasterManagerService {

    String getMasterHost();
    void setMasterHost(String host);

    int getMasterPort();
    void setMasterPort(int port);

    void registerWithMaster(SlaveInfo slaveInfo);

    boolean isMasterUp();

    void notifyFoundPassword(String requestId, String hash, String password);
}
