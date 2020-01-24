package com.processmanager.controllers;

import com.processmanager.models.ComputerModel;
import com.processmanager.models.GenericResponseModel;
import com.processmanager.models.requests.ComputerModelRequest;
import com.processmanager.services.ComputerService;
import com.processmanager.utils.GenericResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/computer")
public class ComputerController {

    @Value("${token}")
    private String tokenKey;

    @Autowired
    private ComputerService computerService;

    @GetMapping
    public GenericResponseModel getAll(){
        return GenericResponseUtil.ok(computerService.getAllActive());
    }

    @GetMapping("/{id}")
    public GenericResponseModel getById(@PathVariable Long id){

        return GenericResponseUtil.ok(null);
    }

    @PostMapping
    public GenericResponseModel create(@RequestBody ComputerModelRequest computerModelRequest){
        try {
            ComputerModel computerModel = computerService.run(computerModelRequest);
            return GenericResponseUtil.ok(computerModel);
        } catch (Exception e) {
            return GenericResponseUtil.setError(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage());
        }
    }

    @GetMapping("/status")
    public GenericResponseModel force(@RequestParam String token) {
        if (token != null) {
            if (token.equals(tokenKey)) {
                try {
                    return GenericResponseUtil.ok(computerService.start());
                } catch (Exception e) {
                    return GenericResponseUtil.setError(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage());
                }
            }
            return GenericResponseUtil.setError(HttpStatus.FORBIDDEN.value(), "Invalid Token");
        }
        return GenericResponseUtil.setError(HttpStatus.BAD_REQUEST.value(), "Empty Token");
    }

    @PutMapping("/{id}")
    public GenericResponseModel edit(@PathVariable Long id, @RequestBody ComputerModel computerModel){

        return GenericResponseUtil.ok(null);
    }
}
