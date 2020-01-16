package com.processmanager.entities;

import com.processmanager.enums.EnumComputerStatus;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "COMPUTER")
public class Computer {

    @Id
    @Column(name = "COMPUTER_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long computerId;

    @Column(name = "ACTIVE")
    private boolean active = true;

    @Column(name = "MASTER")
    private boolean master = false;

    @Column(name = "IP", nullable = false)
    private String ip;

    @Column(name = "PORT", nullable = false, length = 4)
    private int port;

    @Column(name = "PROTOCOL")
    private String protocol;

    @Column(name = "PRIORITY", nullable = false)
    private int priority;

//    @Column(name = "TIMEOUT", nullable = false)
//    @Min(value = 5)
//    private int timeout = 5;

    @Column(name = "LAST_TIME_ALIVE")
    private Timestamp lastTimeAlive = new Timestamp(System.currentTimeMillis());

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private EnumComputerStatus status = EnumComputerStatus.ACTIVE;

//    @OneToMany(targetEntity = Process.class, mappedBy = "computer",
//            fetch = FetchType.EAGER)
//    private List<Process> processes;

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

//    public int getTimeout() {
//        return timeout;
//    }
//
//    public void setTimeout(int timeout) {
//        this.timeout = timeout;
//    }

    public Timestamp getLastTimeAlive() {
        return lastTimeAlive;
    }

    public void setLastTimeAlive(Timestamp lastTimeAlive) {
        this.lastTimeAlive = lastTimeAlive;
    }

    public EnumComputerStatus getStatus() {
        return status;
    }

    public void setStatus(EnumComputerStatus status) {
        this.status = status;
    }

//    public List<Process> getProcesses() {
//        return processes;
//    }
//
//    public void setProcesses(List<Process> processes) {
//        this.processes = processes;
//    }
}