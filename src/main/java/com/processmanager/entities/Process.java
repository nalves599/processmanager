package com.processmanager.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PROCESS")
public class Process {

    @Id
    @Column(name = "PROCESS_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long processId;

    @Column(name = "ACTIVE")
    private boolean active;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PID", nullable = false)
    private int pid;

    @Column(name = "STARTED_AT", nullable = false)
    private Date startedAt;

    @ManyToOne(targetEntity = Computer.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "COMPUTER", referencedColumnName = "COMPUTER_ID")
    private Computer computer;

    @ManyToOne(targetEntity = Command.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "COMMAND", referencedColumnName = "COMMAND_ID")
    private Command command;

    public Process() {
    }

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
}

