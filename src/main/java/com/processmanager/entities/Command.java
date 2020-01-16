package com.processmanager.entities;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Table(name = "COMMAND")
public class Command {

    @Id
    @Column(name = "COMMAND_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long commandId;

    @Column(name = "ACTIVE")
    private boolean active;

    @Column(name = "COMMAND", nullable = false, length = 1000)
    private String command;

    @Column(name = "NAME")
    private String name;


    @OneToMany(targetEntity = Process.class, mappedBy = "command",
            fetch = FetchType.EAGER)
    private List<Process> processes;

    public Command() {
    }

    public Long getCommandId() {
        return commandId;
    }

    public void setCommandId(Long commandId) {
        this.commandId = commandId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }
}
