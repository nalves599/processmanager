package com.processmanager.controllers;

import com.processmanager.models.ComputerModel;
import com.processmanager.models.GenericResponseModel;
import com.processmanager.models.requests.ComputerModelRequest;
import com.processmanager.services.ComputerService;
import com.processmanager.utils.GenericResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/computer")
public class ComputerController {

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

    @GetMapping("/force")
    public GenericResponseModel status(){
        try {
            return GenericResponseUtil.ok(computerService.start());
        } catch (Exception e) {
            return GenericResponseUtil.setError(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public GenericResponseModel edit(@PathVariable Long id, @RequestBody ComputerModel computerModel){

        return GenericResponseUtil.ok(null);
    }
}
