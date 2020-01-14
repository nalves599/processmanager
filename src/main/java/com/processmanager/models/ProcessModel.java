package com.processmanager.models;

import com.processmanager.entities.Command;
import com.processmanager.entities.Computer;
import com.processmanager.enums.EnumProcessStatus;

import java.util.Date;

public class ProcessModel {
    private Long processId;
    private boolean active;
    private String name;
    private int pid;
    private EnumProcessStatus status;
    private Date startedAt;
    private Computer computer;
    private Command command;


    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public EnumProcessStatus getStatus() {
        return status;
    }

    public void setStatus(EnumProcessStatus status) {
        this.status = status;
    }
}
