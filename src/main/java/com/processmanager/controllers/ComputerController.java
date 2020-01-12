package com.processmanager.controllers;

import com.processmanager.models.ComputerModel;
import com.processmanager.models.GenericResponseModel;
import com.processmanager.services.ComputerService;
import com.processmanager.utils.GenericResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/computer")
public class ComputerController {

    @Autowired
    private ComputerService computerService;

    @GetMapping
    public GenericResponseModel getAll(){
        return GenericResponseUtil.ok(computerService.getAll());
    }

    @GetMapping("/{id}")
    public GenericResponseModel getById(@PathVariable Long id){

        return GenericResponseUtil.ok(null);
    }

    @PostMapping
    public GenericResponseModel create(@RequestBody ComputerModel computerModel){
        return GenericResponseUtil.ok(computerService.add(computerModel));
    }

    @PutMapping("/{id}")
    public GenericResponseModel edit(@PathVariable Long id, @RequestBody ComputerModel computerModel){

        return GenericResponseUtil.ok(null);
    }
}
