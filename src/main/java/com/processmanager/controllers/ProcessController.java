package com.processmanager.controllers;

import com.processmanager.models.ComputerModel;
import com.processmanager.models.GenericResponseModel;
import com.processmanager.models.ProcessModel;
import com.processmanager.services.ComputerService;
import com.processmanager.services.ProcessService;
import com.processmanager.utils.GenericResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @GetMapping
    public GenericResponseModel getAll(){
        List<ProcessModel> processModelList = processService.getAll();
        return GenericResponseUtil.ok(processModelList);
    }

    @GetMapping("/{id}")
    public GenericResponseModel getById(@PathVariable Long id){

        return GenericResponseUtil.ok(null);
    }

    @PostMapping
    public GenericResponseModel create(@RequestBody ProcessModel processModel){
        processService.add(processModel);
        return GenericResponseUtil.ok(null);
    }

    @PutMapping("/{id}")
    public GenericResponseModel edit(@PathVariable Long id, @RequestBody ProcessModel processModel){

        return GenericResponseUtil.ok(null);
    }
}
