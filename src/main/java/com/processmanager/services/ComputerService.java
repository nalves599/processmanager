package com.processmanager.services;

import com.processmanager.entities.Computer;
import com.processmanager.entities.Process;
import com.processmanager.enums.EnumComputerStatus;
import com.processmanager.enums.EnumError;
import com.processmanager.enums.EnumProcessStatus;
import com.processmanager.models.ComputerModel;
import com.processmanager.models.requests.ComputerModelRequest;
import com.processmanager.repositories.ComputerRepository;
import com.processmanager.repositories.ProcessRepository;
import com.processmanager.utils.ComputerUtil;
import com.processmanager.utils.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ComputerService {

    private static final Logger Log = LoggerFactory.getLogger(ComputerService.class);


    @Value("${computer.priority-difference}")
    private int difference;

    @Value("${computer.priority-top}")
    private int topPriority;

    @Value("${computer.validate-time}")
    private int validateTime;

    private ComputerRepository computerRepository;
    private ProcessRepository processRepository;

    @Autowired
    public ComputerService(ComputerRepository computerRepository,
                           ProcessRepository processRepository) {
        this.computerRepository = computerRepository;
        this.processRepository = processRepository;
    }

    public List<ComputerModel> getAllActive() {
        List<Computer> computers = computerRepository.findAllByActiveTrueAndStatus(EnumComputerStatus.ACTIVE);
        return ConvertUtil.convertList(computers, ComputerModel.class);
    }

    public ComputerModel add(ComputerModelRequest computerModelRequest) throws Exception {
        if (computerModelRequest.getIp() != null && !computerModelRequest.getIp().isEmpty()) {
            if (computerModelRequest.getPort() > 0 && computerModelRequest.getPort() < 10000) {
                if (computerModelRequest.getProtocol() != null && !computerModelRequest.getProtocol().isEmpty()) {

                    Computer computer = (Computer) ConvertUtil.convert(computerModelRequest, Computer.class);
                    Computer alreadyExist = computerRepository.findComputerByActiveTrueAndIpAndPort(computer.getIp(), computer.getPort());
                    Computer lastComputer = computerRepository.findFirstByActiveTrueAndStatusOrderByPriorityDesc(EnumComputerStatus.ACTIVE);

                    // If already exist
                    if (alreadyExist != null && alreadyExist.isMaster()) {
                        computer = alreadyExist;
                        ComputerUtil.setComputerId(computer.getComputerId());
                    } else if (alreadyExist != null && !alreadyExist.isMaster()) {
                        computerRepository.delete(alreadyExist); // Delete Slave
                        alreadyExist = null;
                        if (lastComputer != null && !lastComputer.getComputerId().equals(computer.getComputerId())) {
                            lastComputer = computerRepository.findFirstByActiveTrueAndStatusOrderByPriorityDesc(EnumComputerStatus.ACTIVE);
                        }
                    }

                    // If LastComputer doesn't exist
                    if (alreadyExist == null && lastComputer == null) {
                        computer.setMaster(true); // Set New Computer As Master
                        computer.setPriority(topPriority); // Set as the top
                    } else if (lastComputer != null && !lastComputer.getComputerId().equals(computer.getComputerId())) {
                        // If last Computer is not the same as the new

                        // If last computer is not active
                        if (!isComputerActive(lastComputer)) {
                            computer.setPriority(lastComputer.getPriority()); // Set Priority Of Last computer on new computer
                            computer.setMaster(lastComputer.isMaster()); // Set Master as true if last was
                            lastComputer.setPriority(computer.getPriority() + difference); // Put Last Computer at the end of the list

                            if (lastComputer.isMaster()) {
                                // Master Change Status
                                lastComputer.setStatus(EnumComputerStatus.WARNING);

                                //Save Last Master with Inactive Computer
                                computerRepository.save(lastComputer);
                            } else {
                                // If Last Computer is Slave remove from repository
                                computerRepository.delete(computer);
                            }
                        } else {
                            computer.setPriority(lastComputer.getPriority() + difference);
                        }

                    }

                    // Save and Return new computer
                    computerRepository.save(computer);
                    ComputerUtil.setComputerId(computer.getComputerId());
                    return (ComputerModel) ConvertUtil.convert(computer, ComputerModel.class);

                } else {
                    throw new Exception(EnumError.INVALID.getValue() + " protocol");
                }
            } else {
                throw new Exception(EnumError.INVALID.getValue() + " port");
            }
        } else {
            throw new Exception(EnumError.INVALID.getValue() + " ip");
        }

    }

    public boolean updateStatusOnDatabase(Long computerId) {
        Computer computer = computerRepository.findComputerByActiveTrueAndComputerId(computerId);
        if (computer != null) {
            computer.setStatus(EnumComputerStatus.ACTIVE);
            computer.setLastTimeAlive(new Timestamp(System.currentTimeMillis())); // Set time to now
            computerRepository.save(computer);
            return true;
        }
        return false;
    }

    public boolean validateOtherComputers(Long computerId) {
        Computer computer = computerRepository.findComputerByActiveTrueAndComputerId(computerId);
        // If this computer exist on database
        if (computer != null) {
            List<Computer> computersToManage = computerRepository.findAllByActiveTrueAndStatusAndPriorityGreaterThanOrderByPriorityAsc(EnumComputerStatus.ACTIVE, computer.getPriority());

            // If This computer isn't the latest
            if (!computersToManage.isEmpty()) {
                // If next computer is not active and is the last one
                if (computersToManage.size() == 1 && !isComputerActive(computersToManage.get(0))) {
//                    // If Last Computer Is Not Active And it's Master
//                    if(computersToManage.get(0).isMaster()) {
//                        computersToManage.get(0).setStatus(EnumComputerStatus.WARNING);
//                    }

                    // Remove it from database and from list
                    computerRepository.delete(computersToManage.get(0));
                    computersToManage.remove(0);
                }
            }

            Computer computerTop = computerRepository.findFirstByActiveTrueAndPriorityLessThanAndStatusOrderByPriorityDesc(computer.getPriority(), EnumComputerStatus.ACTIVE);

            // If This computer is not the top
            if (computerTop != null) {

                // If computer on top of this one is not active
                if (!isComputerActive(computerTop)) {

                    int lastPriority = computer.getPriority();
                    computer.setPriority(computerTop.getPriority());

                    //Move Computers Priorities And Save
                    for (int i = 0; i < computersToManage.size(); i++) {
                        int priority = computersToManage.get(i).getPriority();
                        computersToManage.get(i).setPriority(lastPriority);
                        lastPriority = priority;
                    }

                    // If top computer is master and it's down
                    // TODO : Check Priority Change
                    if (computerTop.isMaster() ) {
                        computer.setMaster(true); // Set New Slave as Master
                        computerTop.setStatus(EnumComputerStatus.WARNING); // Set Top Computer as warning, because processes cannot be ended

                        // Set computer top at the end of the list
                        computerTop.setPriority(lastPriority);
                        computersToManage.add(computerTop);
                    } else {
                        computerRepository.delete(computerTop);
                    }

                    // Save All changes
                    computersToManage.add(computer);
                    computerRepository.saveAll(computersToManage);

                } else {
                    //If This computer Is Master and there is a higher computer
                    if (computer.isMaster()) {
                        // If all processes closed
                        if (closeProcesses()) {
                            computer.setMaster(false);
                            computerTop.setMaster(true);
                            computerRepository.save(computerTop);
                        } else {
                            // TODO : reciew this code
                            computer.setStatus(EnumComputerStatus.WARNING);
                            // TODO : set last priority
                        }
                        computerRepository.save(computer);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /* Private Methods */

    // Return false if all processes not close
    private boolean closeProcesses() {
        List<Process> processes = processRepository.findAllByActiveTrueAndComputer_ComputerId(ComputerUtil.getComputerId());
        boolean status = true;
        // If There is processes not closed on this computer
        if (!processes.isEmpty()) {
            for (Process process : processes) {
                int pid = process.getPid();

                // Get Property
                Optional<ProcessHandle> optionalProcessHandle = ProcessHandle.of(pid);
                // If Process Running
                if (optionalProcessHandle.isPresent()) {
                    ProcessHandle processHandle = optionalProcessHandle.get();

                    // If process doesn't end
                    if (!processHandle.destroy()) {
                        if (processHandle.destroyForcibly()) {
                            process.setStatus(EnumProcessStatus.STOPPED);
                            process.setActive(false);
                        } else {
                            Log.error("Process " + pid + " cannot end, please validate");
                            status = false;
                            process.setStatus(EnumProcessStatus.WARNING);
                        }
                    } else {
                        process.setStatus(EnumProcessStatus.STOPPED);
                        process.setActive(false);
                    }
                } else {
                    // If Process Not Found
                    process.setStatus(EnumProcessStatus.STOPPED);
                    process.setActive(false);
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
//            Simulate deactivate computer
            Random random = new Random();
            boolean isActive = random.nextInt(2) != 0;
            return isActive;
//            return availablePort(computer.getIp(), computer.getPort(), ComputerUtil.getTimeoutTime()); TODO uncomment
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
