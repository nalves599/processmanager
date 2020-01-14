package com.processmanager.repositories;

import com.processmanager.entities.Computer;
import com.processmanager.enums.EnumComputerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface ComputerRepository extends JpaRepository<Computer, Long> {


    List<Computer> findAllByActiveTrue();

    List<Computer> findAllByActiveTrueAndStatus(EnumComputerStatus status);

    // Get All Computers Where Priority Value is Greater
    List<Computer> findAllByActiveTrueAndStatusAndPriorityGreaterThanOrderByPriorityAsc( EnumComputerStatus status, int priority);

    // Get The first Computer where the priority is greater than this one
    Computer findFirstByActiveTrueAndPriorityLessThanAndStatusOrderByPriorityDesc( int priority, EnumComputerStatus status);

    // Get Computer by Id
    Computer findComputerByActiveTrueAndComputerId(Long computerId);

    // Get Most Important Computer
    Computer findFirstByActiveTrueOrderByPriorityAsc();

    // Get Less Important Computer
    Computer findFirstByActiveTrueAndStatusOrderByPriorityDesc(EnumComputerStatus status);

    // Get Computer With the same host and port
    Computer findComputerByActiveTrueAndIpAndPort(String ip, int port);

}
