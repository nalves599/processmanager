package com.processmanager.services;

import com.processmanager.entities.Computer;
import com.processmanager.enums.EnumError;
import com.processmanager.models.ComputerModel;
import com.processmanager.models.requests.ComputerModelRequest;
import com.processmanager.repositories.ComputerRepository;
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

    @Autowired
    public ComputerService(ComputerRepository computerRepository) {
        this.computerRepository = computerRepository;
    }

    public ComputerModel add(ComputerModelRequest computerModelRequest) throws Exception {
        if (computerModelRequest.getIp() != null && !computerModelRequest.getIp().isEmpty()) {
            if (computerModelRequest.getPort() > 0 && computerModelRequest.getPort() < 10000) {
                if (computerModelRequest.getProtocol() != null && !computerModelRequest.getProtocol().isEmpty()) {
                    Computer computer = (Computer) ConvertUtil.convert(computerModelRequest, Computer.class);
                    Computer alreadyExist = computerRepository.findComputerByActiveTrueAndIpAndPort(computer.getIp(), computer.getPort());
                    // If already exist
                    if(alreadyExist != null) {
                        computer = alreadyExist;
                    }

                    Computer lastComputer = computerRepository.findFirstByActiveTrueOrderByPriorityDesc();

                    // If LastComputer doesn't exist
                    if (lastComputer == null) {
                        computer.setMaster(true); // Set New Computer As Master
                        computer.setPriority(topPriority); // Set as the top

                    } else {
                        // If last computer is not active
                        if (!isComputerActive(lastComputer)) {
                            computer.setPriority(lastComputer.getPriority()); // Set Priority Of Last computer on new computer
                            computer.setMaster(lastComputer.isMaster()); // Set Master as true if last was

                            //Remove Last Inactive Computer
                            computerRepository.delete(lastComputer);
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

    public boolean validateComputerStatus(Long computerId) {
        Computer computer = computerRepository.findComputerByActiveTrueAndComputerId(computerId);
        if (computer != null) {
            return isComputerActive(computer);
        } else {
            Log.error("Computer was alive but was deleted");
        }
        return false;
    }

    public List<ComputerModel> getAll() {
        List<Computer> computers = computerRepository.findAllByActiveTrue();
        return ConvertUtil.convertList(computers, ComputerModel.class);
    }

    public boolean updateStatusOnDatabase(Long computerId) {
        Computer computer = computerRepository.findComputerByActiveTrueAndComputerId(computerId);
        if (computer != null) {
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
            List<Computer> computersToManage = computerRepository.findAllByActiveTrueAndPriorityGreaterThanOrderByPriorityAsc(computer.getPriority());

            // If This computer isn't the latest
            if (computersToManage.size() >= 1) {
                // If next computer is not active and is the last one
                if (!isComputerActive(computersToManage.get(0)) && computersToManage.size() == 1) {
                    // Remove it from database and from list
                    computerRepository.delete(computersToManage.get(0));
                    computersToManage.remove(0);
                }
            }

            Computer computerTop = computerRepository.findFirstByActiveTrueAndPriorityLessThanOrderByPriorityDesc(computer.getPriority());

            // If This computer is not the top
            if (computerTop != null) {
                //If This computer Is Master and there is a higher computer
                if (computer.isMaster()) {
                    // TODO : Create Methods on Priority of master change
                }

                // If computer on top of this one is not active
                if (!isComputerActive(computerTop)) {
                    //Remove top computer of this one
                    computerRepository.delete(computerTop);

                    // If top computer is master
                    if (computerTop.isMaster()) {
                        changeMaster(computerTop, computer);
                    }

                    int lastPriority = computer.getPriority();
                    computer.setPriority(computerTop.getPriority());
                    //Move Computers Priorities And Save
                    for (int i = 0; i < computersToManage.size(); i++) {
                        int priority = computersToManage.get(i).getPriority();
                        computersToManage.get(i).setPriority(lastPriority);
                        lastPriority = priority;
                    }

                    // Save All changes
                    computersToManage.add(computer);
                    computerRepository.saveAll(computersToManage);
                }
            }
            return true;
        }
        return false;
    }

    /* Private Methods */

    private Computer changeMaster(Computer oldMasterComputer, Computer newMasterComputer) {
        newMasterComputer.setMaster(true);
        // TODO : methods to change Master
        return newMasterComputer;
    }


    private boolean isComputerActive(Computer computer) {
        Timestamp time = new Timestamp(System.currentTimeMillis() - ((validateTime + ComputerUtil.getOFFSET())));
        Timestamp computerTime = computer.getLastTimeAlive();

        // If Computer dont check the time in database
        if (computerTime == null || time.after(computerTime)) {
//            Simulate deactivate computer
//            Random random = new Random();
//            boolean isActive = random.nextInt(4) != 0;
//            return isActive;
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
