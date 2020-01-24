package com.processmanager.services;

import com.processmanager.entities.Command;
import com.processmanager.models.CommandModel;
import com.processmanager.models.requests.CommandModelRequest;
import com.processmanager.repositories.CommandRepository;
import com.processmanager.utils.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandService {

    private final static Logger Log = LoggerFactory.getLogger(CommandService.class);
    private CommandRepository commandRepository;

    @Autowired
    CommandService(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public List<CommandModel> getAllActive() {
        Log.info("getAllActiveCommands");
        List<Command> commands = commandRepository.findAllByActiveTrue();
        return ConvertUtil.convertList(commands, CommandModel.class);
    }

    public CommandModel add(CommandModelRequest commandModelRequest) {
        Log.info("addCommand");
        Command command = (Command) ConvertUtil.convert(commandModelRequest, Command.class);

        commandRepository.save(command);
        Log.debug("command - " + commandModelRequest.toString());
        return (CommandModel) ConvertUtil.convert(command, CommandModel.class);
    }

    public CommandModel getCommandById(Long commandId) {
        Log.info("getCommandById" + commandId);
        Command command = commandRepository.findCommandByActiveTrueAndCommandIdEquals(commandId);
        Log.debug("command - " + command.toString());
        return (CommandModel) ConvertUtil.convert(command, CommandModel.class);
    }

}
