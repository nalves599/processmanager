package com.processmanager.beans;

import com.processmanager.models.requests.ComputerModelRequest;
import com.processmanager.services.ComputerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ComputerBean {

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private ComputerService computerService;

    @Bean
    public void initComputer(){
        ComputerModelRequest computerModelRequest = new ComputerModelRequest();
        computerModelRequest.setIp("localhost"); // TODO : Change IP
        computerModelRequest.setPort(serverPort);
        computerModelRequest.setProtocol("http");

        try {
            computerService.add(computerModelRequest);
        } catch (Exception e) {
            System.err.println("Failed to Initialize");
        }
    }
}
