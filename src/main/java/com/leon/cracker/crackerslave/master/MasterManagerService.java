package com.leon.cracker.crackerslave.master;

public interface MasterManagerService {

    String getMasterHost();
    void setMasterHost(String host);

    int getMasterPort();
    void setMasterPort(int port);

    void registerWithMaster(SlaveInfo slaveInfo);
}
