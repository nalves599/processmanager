package com.processmanager.services;

import com.processmanager.entities.Process;
import com.processmanager.models.ProcessModel;
import com.processmanager.repositories.ProcessRepository;
import com.processmanager.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessService {

    private ProcessRepository processRepository;

    @Autowired
    public ProcessService(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    public List<ProcessModel> getAll(){
        List<Process> processes = processRepository.findAllByActiveTrue();
        return ConvertUtil.convertList(processes, ProcessModel.class);
    }

    public ProcessModel add(ProcessModel processModel){
        Process process = (Process) ConvertUtil.convert(processModel, Process.class);
        processRepository.save(process);
        return (ProcessModel) ConvertUtil.convert(process, ProcessModel.class);
    }
}
