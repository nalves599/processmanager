package com.processmanager.beans;

import com.processmanager.models.requests.ComputerModelRequest;
import com.processmanager.services.ComputerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ComputerBean {

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private ComputerService computerService;

//    @Bean
    public void initComputer(){
        ComputerModelRequest computerModelRequest = null;
        try {
            computerModelRequest = new ComputerModelRequest();
            computerModelRequest.setIp(InetAddress.getLocalHost().getHostName());
            computerModelRequest.setPort(serverPort);
            computerModelRequest.setProtocol("http");
        } catch (UnknownHostException e) {
            System.exit(0);
        }

        try {
            computerService.add(computerModelRequest);
            // TODO : Validate Processes
        } catch (Exception e) {
            System.err.println("Failed to Initialize");
        }
    }
}
