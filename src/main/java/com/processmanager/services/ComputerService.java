package com.processmanager.services;

import com.processmanager.entities.Computer;
import com.processmanager.entities.Process;
import com.processmanager.models.ComputerModel;
import com.processmanager.models.ProcessModel;
import com.processmanager.repositories.ComputerRepository;
import com.processmanager.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComputerService {

    private ComputerRepository computerRepository;

    @Autowired
    public ComputerService(ComputerRepository computerRepository) {
        this.computerRepository = computerRepository;
    }

    public ComputerModel add(ComputerModel computerModel) {
        Computer computer = (Computer) ConvertUtil.convert(computerModel, Computer.class);
        computerRepository.save(computer);
        return (ComputerModel) ConvertUtil.convert(computer, ComputerModel.class);
    }

    public List<ComputerModel> getAll() {
        List<Computer> computers = computerRepository.findAllByActiveTrue();
        return ConvertUtil.convertList(computers, ComputerModel.class);
    }
}
