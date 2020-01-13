package com.processmanager.repositories;

import com.processmanager.entities.Computer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface ComputerRepository extends JpaRepository<Computer, Long> {

    void deleteComputerByComputerId(Long computerId);

    List<Computer> findAllByActiveTrue();

    // Get All Computers Where Priority Value is Greater
    List<Computer> findAllByActiveTrueAndPriorityGreaterThanOrderByPriorityAsc(int priority);

    // Get The first Computer where the priority is greater than this one
    Computer findFirstByActiveTrueAndPriorityLessThanOrderByPriorityDesc(int priority);

    // Get Computer by Id
    Computer findComputerByActiveTrueAndComputerId(Long computerId);

    // Get Most Important Computer
    Computer findFirstByActiveTrueOrderByPriorityAsc();

    // Get Less Important Computer
    Computer findFirstByActiveTrueOrderByPriorityDesc();

    // Get Computer With the same host and port
    Computer findComputerByActiveTrueAndIpAndPort(String ip, int port);

}
