package com.leon.cracker.crackerslave.services;

import com.leon.cracker.crackerslave.models.FoundPasswordRequest;
import com.leon.cracker.crackerslave.models.SlaveCrackingRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class CrackingService implements ICrackingService {

    private static final Logger logger = LoggerFactory.getLogger(CrackingService.class);

    private IMasterManagerService masterManagerService;
    private ISlaveMetaDataService slaveMetaDataService;


    @Autowired
    public void setMasterManagerService(IMasterManagerService masterManagerService) {
        this.masterManagerService = masterManagerService;
    }

    @Autowired
    public void setSlaveMetaDataService(ISlaveMetaDataService slaveMetaDataService) {
        this.slaveMetaDataService = slaveMetaDataService;
    }

    @Override
    @Async
    public void crack(SlaveCrackingRequest slaveCrackingRequest) {

        slaveMetaDataService.addRequest(slaveCrackingRequest);

        IntStream.rangeClosed(slaveCrackingRequest.getStart(), slaveCrackingRequest.getEnd())
                .boxed()
                .map(this::mapPlainIntToPhoneNumber)
                .forEach(phoneNumber -> {
                    String hash = MD5Hash(phoneNumber);

                    if (slaveCrackingRequest.getHashes().contains(hash)) {
                        logger.info("Found a match! {} -> {}", phoneNumber, hash);
                        masterManagerService.notifyFoundPassword(new FoundPasswordRequest(slaveCrackingRequest.getRequestId(), hash, phoneNumber));
                    }

                });

        masterManagerService.notifySlaveIsDone(slaveCrackingRequest);
    }

    private String mapPlainIntToPhoneNumber(int number) {

        return new StringBuilder()
                .append('0')
                .append(number)
                .insert(3, '-')
                .toString();

    }

    private String MD5Hash(String phoneNumber) {
        return DigestUtils
                .md5Hex(phoneNumber).toUpperCase();
    }
}
