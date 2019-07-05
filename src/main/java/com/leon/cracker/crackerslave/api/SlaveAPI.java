package com.leon.cracker.crackerslave.api;

import com.leon.cracker.crackerslave.models.SlaveCrackingRequest;
import com.leon.cracker.crackerslave.services.ICrackingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/slave")
public class SlaveAPI {

    private static final Logger logger = LoggerFactory.getLogger(SlaveAPI.class);

    private ICrackingService crackingService;

    @Autowired
    public void setCrackingService(ICrackingService crackingService) {
        this.crackingService = crackingService;
    }

    @PostMapping("/crack")
    public ResponseEntity<String> crack(@RequestBody @Valid SlaveCrackingRequest slaveCrackingRequest){
        logger.info("Received {}", slaveCrackingRequest);
        crackingService.crack(slaveCrackingRequest);
        return ResponseEntity.ok("Cracking request in process");
    }
}
