package org.logger.config;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class PolopolyIndexserverLogFileFinder extends LogFileFinder {
    private static final Logger LOGGER = Logger.getLogger(PolopolyIndexserverLogFileFinder.class.getName());
    private final List<String> logs;

    public PolopolyIndexserverLogFileFinder() throws ConfigException {
        ProcessList processList = new ProcessList(Runtime.getRuntime());
        Iterator<ProcessInfo> processIterator = processList.processes();
        ArrayList<String> indexserverLogs = new ArrayList<String>();

        while (processIterator.hasNext()) {
            ProcessInfo process = processIterator.next();
            if (isIndexserver(process)) {
                Properties loggingProperties = getLoggingProperties(process);

                String logFile = loggingProperties.getProperty("java.util.logging.FileHandler.pattern");
                indexserverLogs.add(logFile);
            }
        }
        logs = indexserverLogs;
    }

    private Properties getLoggingProperties(ProcessInfo process) throws ConfigException {
        String path = getVariableFromProcess("java.util.logging.config.file", process);
        Properties loggingProperties = new Properties();
        try {
            loggingProperties.load(new FileReader(path));
            return loggingProperties;
        } catch (Exception e) {
            throw new ConfigException("Could not load logging.properties", e);
        }
    }

    private String getVariableFromProcess(String variable, ProcessInfo process) throws ConfigException {

        for (String arg : process.getCommand()) {
            String prefix = "-D" + variable + "=";
            if (arg.startsWith(prefix)) {
                return arg.substring(new String(prefix).length());
            }
        }
        LOGGER.warning("Could not find variable " + variable);
        throw new ConfigException("Could not find variable " + variable);
    }

    private boolean isIndexserver(ProcessInfo process) {
        return process.getCommand().contains("-Dmodule.name=indexserver");
    }

    @Override
    public List<String> getLogs() {
        return logs;
    }
}
