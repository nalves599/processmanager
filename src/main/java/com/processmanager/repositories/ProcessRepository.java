package com.processmanager.repositories;

import com.processmanager.entities.Computer;
import com.processmanager.entities.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
    List<Process> findAllByActiveTrue();

    // Get All Processes by Computer Id
    List<Process> findAllByActiveTrueAndComputer_ComputerId(Long computer_computerId);
}
