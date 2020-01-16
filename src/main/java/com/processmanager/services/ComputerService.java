package com.processmanager.services;

import com.processmanager.entities.Command;
import com.processmanager.entities.Computer;
import com.processmanager.entities.Process;
import com.processmanager.enums.EnumComputerStatus;
import com.processmanager.enums.EnumProcessStatus;
import com.processmanager.models.ComputerModel;
import com.processmanager.models.requests.ComputerModelRequest;
import com.processmanager.repositories.CommandRepository;
import com.processmanager.repositories.ComputerRepository;
import com.processmanager.repositories.ProcessRepository;
import com.processmanager.utils.ComputerUtil;
import com.processmanager.utils.ConvertUtil;
import com.processmanager.utils.ReaderThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ComputerService {

    private static final Logger Log = LoggerFactory.getLogger(ComputerService.class);


    @Value("${computer.priority-difference}")
    private int difference;

    @Value("${server.port}")
    private int serverPort;

    @Value("${computer.priority-top}")
    private int topPriority;

    @Value("${computer.validate-time}")
    private int validateTime;

    @Value("${command.file-path}")
    private String commandPath;

    private ComputerRepository computerRepository;
    private ProcessRepository processRepository;
    private CommandRepository commandRepository;

    @Autowired
    public ComputerService(ComputerRepository computerRepository,
                           ProcessRepository processRepository,
                           CommandRepository commandRepository) {
        this.computerRepository = computerRepository;
        this.processRepository = processRepository;
        this.commandRepository = commandRepository;
    }

    public ComputerModel run(ComputerModelRequest computerModelRequest) {
        Computer computer = (Computer) ConvertUtil.convert(computerModelRequest, Computer.class);
        computer = validateExistence(computer);

        // If Computer Not Exist
        if (computer.getComputerId() == null) {
            computer = createComputer(computer);
        }
        // Validate My Status
        computer = validateMyStatus(computer);
        computer = validateTopConnection(computer);

        return null;
    }

    public ComputerModel start() {
        ComputerModelRequest computerModelRequest = null;

        try {
            computerModelRequest = new ComputerModelRequest();
            computerModelRequest.setIp(InetAddress.getLocalHost().getHostName());
            computerModelRequest.setPort(serverPort);
            computerModelRequest.setProtocol("http");

            return run(computerModelRequest);
        } catch (UnknownHostException e) {
            Log.error("Error Getting HostName");
        }

        return null;
    }

    public List<ComputerModel> getAllActive() {
        List<Computer> computers = computerRepository.findAllByActiveTrueAndStatusOrderByPriorityDesc(EnumComputerStatus.ACTIVE);
        return ConvertUtil.convertList(computers, ComputerModel.class);
    }

    /* Private Methods */

    // Return Computer Object
    private Computer validateExistence(Computer computer) {
        Computer alreadyExist = computerRepository.findComputerByActiveTrueAndIpAndPort(computer.getIp(), computer.getPort());

        ComputerUtil.setComputerId(computer.getComputerId());
        ComputerUtil.setIp(computer.getIp());
        ComputerUtil.setPort(computer.getPort());

        if (alreadyExist != null) {
            if (alreadyExist.getStatus().equals(EnumComputerStatus.ACTIVE)) {
                return alreadyExist;
            }
            if (alreadyExist.isMaster()) {
                // If can't close all processes
                if (!closeProcesses()) {
                    // TODO : IF can't close all processes
                    Log.error("Cannot close all processes");
                }
            }
            computerRepository.delete(alreadyExist); // Remove it from database
        }

        return computer;
    }

    private Computer createComputer(Computer computer) {
        // Get last computer active
        Computer lastComputer = computerRepository.findFirstByActiveTrueAndStatusOrderByPriorityAsc(EnumComputerStatus.ACTIVE);

        if (lastComputer != null) {
            computer.setMaster(false);
            computer.setPriority(lastComputer.getPriority() + difference);
        } else {
            computer.setMaster(true);
            computer.setPriority(topPriority);
        }

        computerRepository.save(computer); // Save on Database

        return computer;
    }

    private Computer validateMyStatus(Computer thisComputer) {
        int thisComputerPriority = thisComputer.getPriority();
        Computer mostImportantComputer = computerRepository.findFirstByActiveTrueAndStatusOrderByPriorityDesc(EnumComputerStatus.ACTIVE);

        thisComputer.setLastTimeAlive(new Timestamp(System.currentTimeMillis()));
        thisComputer.setStatus(EnumComputerStatus.ACTIVE);

        // If this computer is the most important / Master
        if (mostImportantComputer.getPriority() == thisComputerPriority) {
            thisComputer.setMaster(true);
            startAndCheckProcesses(thisComputer);
        } else {
            // If This computer was the master
            if (thisComputer.isMaster()) {
                if (!closeProcesses()) {
                    // TODO : IF can't close all processes
                    Log.error("Cannot Close Processes");
                } else {
                    thisComputer.setMaster(false);
                }
            }
        }

        computerRepository.save(thisComputer);

        return thisComputer;
    }

    private Computer validateTopConnection(Computer thisComputer) {
        List<Computer> computers = computerRepository.findAllByActiveTrueAndStatusOrderByPriorityDesc(EnumComputerStatus.ACTIVE);

        if (!computers.isEmpty()) {
            int thisComputerIndex = -1;
            for (int i = 0; i < computers.size(); i++) {
                if (computers.get(i).getComputerId().equals(thisComputer.getComputerId())) {
                    thisComputer = computers.get(i);
                    thisComputerIndex = i;
                }
            }

            if (computers.size() > 1) {
                // If this computer is not master
                if (thisComputerIndex > 0) {
                    int topComputerIndex = thisComputerIndex - 1;
                    if (!isComputerActive(computers.get(topComputerIndex))) {

                        for (int i = thisComputerIndex; i < computers.size(); i++) {
                            int lastPriority = computers.get(i - 1).getPriority();
                            computers.get(i - 1).setPriority(computers.get(i).getPriority());
                            computers.get(i).setPriority(lastPriority);
                        }

                        // If Top Computer is Master
                        if (topComputerIndex == 0) {
                            computers.get(topComputerIndex).setStatus(EnumComputerStatus.WARNING);
                        } else {
                            computers.get(topComputerIndex).setStatus(EnumComputerStatus.SHUTDOWN);
                        }
                    }
                }
                //TODO : Add method to check if all slaves are not dead
            }
            return computers.get(thisComputerIndex);
        }
        return thisComputer;
    }

    private void startAndCheckProcesses(Computer computer) {
        List<Command> commands = commandRepository.findAllByActiveTrue();

        for (Command command : commands) {
            if (!isThisCommandRunning(computer, command)) {
                java.lang.Process process = null;
                try {
                    process = Runtime.getRuntime().exec(command.getCommand());
                    ReaderThreadUtil readerThread = new ReaderThreadUtil(process, command.getName(), commandPath);
                    readerThread.start();

                    Process p = new Process();
                    p.setPid(process.pid());
                    p.setComputer(computer);
                    p.setCommand(command);

                    processRepository.save(p);
                } catch (IOException e) {
                    Log.error("Error To Execute Command");
                }
            }
        }

    }

    private boolean isThisCommandRunning(Computer computer, Command command) {
        List<Process> processes = command.getProcesses();
        for (Process process : processes) {
            if (process.getComputer().getComputerId().equals(computer.getComputerId()) && process.getCommand().getCommandId().equals(command.getCommandId())) {
                Optional<ProcessHandle> optionalProcessHandle = ProcessHandle.of(process.getPid());
                if(optionalProcessHandle.isPresent()) {
                    return true;
                } else {
                    processRepository.delete(process);
                }
            }
        }
        return false;
    }

    // Return false if all processes not close
    private boolean closeProcesses() {
        List<Process> processes = processRepository.findAllByActiveTrueAndComputer_ComputerId(ComputerUtil.getComputerId());
        boolean status = true;
        // If There is processes not closed on this computer
        if (!processes.isEmpty()) {
            for (Process process : processes) {
                Long pid = process.getPid();

                // Get Property
                Optional<ProcessHandle> optionalProcessHandle = ProcessHandle.of(pid);
                // If Process Running
                if (optionalProcessHandle.isPresent()) {
                    ProcessHandle processHandle = optionalProcessHandle.get();

                    // If process doesn't end
                    if (!processHandle.destroy()) {
                        if (processHandle.destroyForcibly()) {
                            processRepository.delete(process);
                            processes.remove(process);
                        } else {
                            Log.error("Process " + pid + " cannot end, please validate");
                            status = false;
                            process.setStatus(EnumProcessStatus.WARNING);
                        }
                    } else {
                        processRepository.delete(process);
                        processes.remove(process);
                    }
                } else {
                    // If Process Not Found
                    processRepository.delete(process);
                    processes.remove(process);
                }
            }

            // Save All Changes
            processRepository.saveAll(processes);
        }

        return status;

    }

    private boolean isComputerActive(Computer computer) {
        Timestamp time = new Timestamp(System.currentTimeMillis() - ((validateTime + ComputerUtil.getOFFSET())));
        Timestamp computerTime = computer.getLastTimeAlive();

        // If Computer dont check the time in database
        if (computerTime == null || time.after(computerTime)) {
            return availablePort(computer.getIp(), computer.getPort(), ComputerUtil.getTimeoutTime());
        }
        return true;

    }

    private boolean availablePort(String host, int port, int timeout) {
        Log.debug("Testing host " + host + ": port" + port);
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        Socket socket = new Socket();

        try {
            socket.connect(socketAddress, timeout);
            // If the code makes it this far without an exception it means
            // something is using the port and has responded.
            Log.debug("host " + host + " and Port " + port + " is available");
            socket.close();
            return true;
        } catch (SocketTimeoutException exception) {
            Log.warn("SocketTimeoutException " + host + ":" + port + ". " + exception.getMessage());
        } catch (IOException exception) {
            Log.warn("IOException - Unable to connect to " + host + ":" + port + ". " + exception.getMessage());
        }
        return false;
    }

}
