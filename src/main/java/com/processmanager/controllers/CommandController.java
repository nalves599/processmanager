package com.processmanager.controllers;

import com.processmanager.models.GenericResponseModel;
import com.processmanager.models.requests.CommandModelRequest;
import com.processmanager.services.CommandService;
import com.processmanager.utils.GenericResponseUtil;
import com.processmanager.validations.CommandValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/command")
public class CommandController {

    @Autowired
    private CommandService commandService;

    @GetMapping
    private GenericResponseModel getAll() {
        return GenericResponseUtil.ok(commandService.getAllActive());
    }

    @GetMapping("command/{id}")
    private GenericResponseModel getById(@PathVariable Long id) {
        return GenericResponseUtil.ok(commandService.getCommandById(id));
    }

    @PostMapping
    private GenericResponseModel create(@RequestBody CommandModelRequest commandModelRequest) {
        GenericResponseModel genericResponseModel = CommandValidation.validate(commandModelRequest);
        if (genericResponseModel != null) {
            return genericResponseModel;
        }
        return GenericResponseUtil.ok(commandService.add(commandModelRequest));
    }
}
