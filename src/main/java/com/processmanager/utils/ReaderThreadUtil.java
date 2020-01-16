package com.processmanager.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;

public class ReaderThreadUtil extends Thread {
    private static final Logger Log = LoggerFactory.getLogger(ReaderThreadUtil.class);
    private Process p;
    private String name;
    private String commandPath;

    public ReaderThreadUtil(Process inp, String name, String commandPath) {
        p = inp;
        this.name = name;
        this.commandPath = commandPath;
    }

    @Override
    public void run() {
        try {
            Long currentTime = new Date(System.currentTimeMillis()).getTime();
            String filePath = commandPath + "LOG-" + name + "-" + currentTime + ".log";
            File ff = new File(filePath);
            if (ff.createNewFile()) {
                Log.info("New File Created " + ff.getPath());
                FileWriter myWriter = new FileWriter(filePath);
                String line;
                BufferedReader bri = new BufferedReader
                        (new InputStreamReader(p.getInputStream()));
                BufferedReader bre = new BufferedReader
                        (new InputStreamReader(p.getErrorStream()));
                while ((line = bri.readLine()) != null) {
                    myWriter.write(line + "\n");
                }
                myWriter.close();
                bri.close();
            }

        } catch (IOException e) {
            Log.error("Error Reading Process");
        }
    }
}
