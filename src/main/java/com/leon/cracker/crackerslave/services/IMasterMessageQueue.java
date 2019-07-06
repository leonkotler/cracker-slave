package com.leon.cracker.crackerslave.services;

import com.leon.cracker.crackerslave.models.FoundPasswordRequest;

public interface IMasterMessageQueue {

    void addFoundPasswordRequest(FoundPasswordRequest foundPasswordRequest);
    FoundPasswordRequest pollFoundPasswordRequest();

}
