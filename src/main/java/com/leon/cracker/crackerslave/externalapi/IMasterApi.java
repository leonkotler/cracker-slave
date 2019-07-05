package com.leon.cracker.crackerslave.externalapi;

import com.leon.cracker.crackerslave.models.FoundPasswordRequest;

import java.net.URI;

public interface IMasterApi {
    boolean isMasterUp(URI masterURI);
    void foundPassword(URI masterURI, FoundPasswordRequest foundPasswordRequest);
}
