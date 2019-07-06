package com.leon.cracker.crackerslave;

import com.leon.cracker.crackerslave.models.RegisterWithMasterRequest;
import com.leon.cracker.crackerslave.services.IMasterManagerService;
import com.leon.cracker.crackerslave.services.ISlaveMetaDataService;
import com.leon.cracker.crackerslave.models.SlaveInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class SlaveRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SlaveRunner.class);

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

    @Value("${slave.name}")
    private String slaveName;

    @Autowired
    private Environment environment;


    @Override
    public void run(String... args) throws Exception {

        String[] hostAndPort = args[1].split(":");

        masterManagerService.setMasterHost(hostAndPort[0]);
        masterManagerService.setMasterPort(Integer.parseInt(hostAndPort[1]));

        logger.info("Slave [{}] initiated successfully by master at: {}", slaveName, hostAndPort);

        SlaveInfo slaveInfo = new SlaveInfo(slaveName, getLocalHost(), getPort());
        slaveMetaDataService.setSlaveInfo(slaveInfo);
        RegisterWithMasterRequest registerRequest = new RegisterWithMasterRequest(slaveInfo, slaveMetaDataService.getCurrentRequests());

        masterManagerService.registerWithMaster(registerRequest);

    }

    private String getLocalHost() {

        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return host;
    }

    private int getPort(){
        return Integer.parseInt(environment.getProperty("local.server.port"));
    }
}
