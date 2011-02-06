package example.catalina.config;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class LogFileFinder {
    private static final Logger LOGGER = Logger.getLogger(LogFileFinder.class.getName());
    private final List<String> logs;

    public LogFileFinder(Runtime runtime) throws ConfigException {
        ProcessList processList = new ProcessList(runtime);
        Iterator<ProcessInfo> processIterator = processList.processes();
        ArrayList<String> tomcatLogs = new ArrayList<String>();

        while (processIterator.hasNext()) {
            ProcessInfo process = processIterator.next();
            if (isTomcat(process)) {
                String catalinaBase = getCatalinaBase(process);
                String catalinaHome = getCatalinaHome(process);
                Properties loggingProperties = getLoggingProperties(process);

                String logDir = loggingProperties.getProperty("1catalina.org.apache.juli.FileHandler.directory");

                logDir = logDir.replaceFirst("\\$\\{catalina\\.base\\}", catalinaBase);

                logDir = logDir.replaceFirst("\\$\\{catalina\\.home\\}", catalinaHome);

                String logFile = logDir + "/catalina.out";
                tomcatLogs.add(logFile);
            }
        }
        logs = tomcatLogs;
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

    private String getCatalinaBase(ProcessInfo process) throws ConfigException {
        return getVariableFromProcess("catalina.base", process);
    }

    private String getCatalinaHome(ProcessInfo process) throws ConfigException {
        return getVariableFromProcess("catalina.home", process);
    }

    private boolean isTomcat(ProcessInfo process) {
        int size = process.getCommand().size();
        return size > 1 && process.getCommand().get(size - 2).equals("org.apache.catalina.startup.Bootstrap");
    }

    public List<String> getLogs() {
        return logs;
    }
}
