package com.processmanager.schedullers;

import com.processmanager.enums.EnumError;
import com.processmanager.services.ComputerService;
import com.processmanager.utils.ComputerUtil;
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
//        List<ComputerModel> computers = computerService.getAll();
//        for(ComputerModel computerModel : computers) {
////
//            if (computerService.updateStatusOnDatabase(computerModel.getComputerId())){
//                computerService.validateOtherComputers(computerModel.getComputerId());
//            } else {
//                Log.error(EnumError.INVALID + " computer");
//            }
//
//        }

        if (computerService.updateStatusOnDatabase(ComputerUtil.getComputerId())) {
            computerService.validateOtherComputers(ComputerUtil.getComputerId());
        } else {
            Log.error(EnumError.INVALID + " computer");
        }
    }

    /* Private Methods */

}