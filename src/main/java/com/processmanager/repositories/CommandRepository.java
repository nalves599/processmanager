package com.processmanager.repositories;

import com.processmanager.entities.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {
    List<Command> findAllByActiveTrue();

    Command findCommandByActiveTrueAndCommandIdEquals(Long commandId);
}
