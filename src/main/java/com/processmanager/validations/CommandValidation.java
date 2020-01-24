package com.processmanager.validations;

import com.processmanager.models.GenericResponseModel;
import com.processmanager.models.requests.CommandModelRequest;
import com.processmanager.utils.GenericResponseUtil;
import org.springframework.http.HttpStatus;

public class CommandValidation {

    public static GenericResponseModel validate(CommandModelRequest commandModelRequest) {
        if (commandModelRequest == null) {
            return GenericResponseUtil.setError(HttpStatus.BAD_REQUEST.value(), "Empty Request");
        } else if (commandModelRequest.getCommand().isEmpty()) {
            return GenericResponseUtil.setError(HttpStatus.BAD_REQUEST.value(), "Empty Command");
        } else if (commandModelRequest.getName().isEmpty()) {
            return GenericResponseUtil.setError(HttpStatus.BAD_REQUEST.value(), "Empty Name");
        } else if (validateCommand(commandModelRequest.getCommand())) {
            return GenericResponseUtil.setError(HttpStatus.BAD_REQUEST.value(), "Invalid Command, only one accepted");
        }
        // TODO : Add Name Validation
        return null;
    }

    /* PRIVATE Methods */
    private static boolean validateCommand(String command) {
        boolean moreThanOneCommand = (command.contains(" & ") || command.contains(" | ") || command.contains(" && ") || command.contains(" || "));
        return moreThanOneCommand;
    }
}
