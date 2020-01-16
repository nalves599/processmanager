package com.processmanager.beans;

import com.processmanager.services.ComputerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ComputerBean {


    @Autowired
    private ComputerService computerService;

}
