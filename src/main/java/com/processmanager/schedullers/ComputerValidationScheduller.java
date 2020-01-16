package com.processmanager.schedullers;

import com.processmanager.services.ComputerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ComputerValidationScheduller {

    private static final Logger Log = LoggerFactory.getLogger(ComputerValidationScheduller.class);

    @Autowired
    private ComputerService computerService;

    @Scheduled(fixedDelayString = "${computer.validate-time}") //60 seconds
    private void validateMachine() {
        computerService.start();

    }

    /* Private Methods */

}
