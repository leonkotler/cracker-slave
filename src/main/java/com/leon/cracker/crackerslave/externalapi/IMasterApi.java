package com.leon.cracker.crackerslave.externalapi;

import com.leon.cracker.crackerslave.models.FoundPasswordRequest;
import com.leon.cracker.crackerslave.models.SlaveCrackingRequest;
import com.leon.cracker.crackerslave.models.SlaveDoneRequest;
import com.leon.cracker.crackerslave.models.SlaveInfo;

import java.net.URI;

public interface IMasterApi {
    boolean isMasterUp(URI masterURI);
    void foundPassword(URI masterURI, FoundPasswordRequest foundPasswordRequest);

    void slaveIsDone(URI masterURI, SlaveDoneRequest slaveDoneRequest);
}
