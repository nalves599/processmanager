package com.processmanager.models;

import com.processmanager.enums.EnumComputerStatus;

import java.util.Date;
import java.util.List;

public class ComputerModel {

    private Long computerId;
    private boolean active;
    private boolean master;
    private String ip;
    private int port;
    private String protocol;
    private int priority;
    private int timeout;
    private Date lastTimeAlive;
    private EnumComputerStatus status;
    private List<Process> processes;

    public Long getComputerId() {
        return computerId;
    }

    public void setComputerId(Long computerId) {
        this.computerId = computerId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Date getLastTimeAlive() {
        return lastTimeAlive;
    }

    public void setLastTimeAlive(Date lastTimeAlive) {
        this.lastTimeAlive = lastTimeAlive;
    }

    public EnumComputerStatus getStatus() {
        return status;
    }

    public void setStatus(EnumComputerStatus status) {
        this.status = status;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }
}
