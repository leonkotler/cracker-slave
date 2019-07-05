package com.leon.cracker.crackerslave.externalapi;

import com.leon.cracker.crackerslave.models.FoundPasswordRequest;
import com.leon.cracker.crackerslave.models.HealthStatus;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class MasterApi implements IMasterApi {

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

        restTemplate.postForEntity(masterURI + "/master/found-password", foundPasswordRequest, null);
    }
}
