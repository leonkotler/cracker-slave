package com.leon.cracker.crackerslave.externalapi;

import com.leon.cracker.crackerslave.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class MasterApi implements IMasterApi {

    private static final Logger logger = LoggerFactory.getLogger(MasterApi.class);

    @Override
    public boolean isMasterUp(URI masterURI) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            return restTemplate
                    .getForEntity(masterURI + "/actuator/health", HealthStatus.class)
                    .getStatusCode()
                    .equals(HttpStatus.OK);

        } catch (ResourceAccessException exception) {
            return false;
        }
    }

    @Override
    @Async
    public void foundPassword(URI masterURI, FoundPasswordRequest foundPasswordRequest) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.postForEntity(masterURI + "/master/found-password", foundPasswordRequest, null);

        } catch (ResourceAccessException exception) {
            logger.error("FoundPassword request failed! Message is: {}", exception.getMessage());
        }
    }

    @Override
    public void slaveIsDone(URI masterURI, SlaveDoneRequest slaveDoneRequest) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.postForEntity(masterURI + "/master/done-processing-request", slaveDoneRequest, null);

        } catch (ResourceAccessException exception) {
            logger.error("SlaveIsDone request failed! Message is: {}", exception.getMessage());
        }
    }


}
