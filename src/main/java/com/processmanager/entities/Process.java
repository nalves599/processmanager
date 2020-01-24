package com.processmanager.entities;

import com.processmanager.enums.EnumProcessStatus;

import javax.persistence.*;

@Entity
@Table(name = "PROCESS")
public class Process {

    @Id
    @Column(name = "PROCESS_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "process_sequence")
    private Long processId;

    @Column(name = "ACTIVE")
    private boolean active = true;

    @Column(name = "PID", nullable = false)
    private Long pid;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private EnumProcessStatus status = EnumProcessStatus.RUNNING;

    @ManyToOne(targetEntity = Computer.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "COMPUTER", referencedColumnName = "COMPUTER_ID")
    private Computer computer;

    @ManyToOne(targetEntity = Command.class, fetch = FetchType.EAGER)
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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
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

