package com.leon.cracker.crackerslave.master;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class SlaveInfo  {


    private String name;
    private String host;
    private int port;

    public SlaveInfo() {
    }

    public SlaveInfo(String name, String host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "SlaveInfo{" +
                "name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlaveInfo slaveInfo = (SlaveInfo) o;
        return port == slaveInfo.port &&
                name.equals(slaveInfo.name) &&
                host.equals(slaveInfo.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, host, port);
    }
}
