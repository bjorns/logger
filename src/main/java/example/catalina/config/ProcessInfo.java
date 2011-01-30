package example.catalina.config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessInfo {
    private static final Logger LOGGER = Logger.getLogger(ProcessInfo.class.getName());

    // At least on osx.
    private static final int UID = 0;
    private static final int PID = 1;
    private static final int PARENT_PID = 2;
    private static final int RECENT_CPU_USAGE = 3;
    private static final int START_TIME = 4;
    private static final int TTY = 5;
    private static final int ELAPSED_CPU_USAGE = 6;
    private static final int COMMAND = 7;

    private int uid;
    private int pid;
    private int parentPid;
    private int recentCpuUsage;
    private Date startTime;
    private String tty;
    private Date elapsedCpuUsage;
    private ArrayList<String> command;

    public ProcessInfo(String line) {
        DateFormat formatter = new SimpleDateFormat("MM:mm.ss");

        String[] parts = line.split(" ");
        int pos = 0;
        for (String part : parts) {
            if (part != null && !part.equals("")) {
                switch (pos) {
                case UID:
                    uid = Integer.valueOf(part);
                    break;
                case PID:
                    pid = Integer.valueOf(part);
                    break;
                case PARENT_PID:
                    parentPid = Integer.valueOf(part);
                    break;
                case RECENT_CPU_USAGE:
                    recentCpuUsage = Integer.valueOf(part);
                    break;
                case START_TIME:
                    try {
                        startTime = formatter.parse(part);
                    } catch (ParseException e) {
                        LOGGER.log(Level.WARNING, "Could not parse " + part, e);
                    }
                    break;
                case TTY:
                    tty = part;
                    break;
                case ELAPSED_CPU_USAGE:
                    try {
                        startTime = formatter.parse(part);
                    } catch (ParseException e) {
                        LOGGER.log(Level.WARNING, "Could not parse " + part, e);
                    }
                    break;
                case COMMAND:
                    command = new ArrayList<String>();
                    command.add(part);
                    break;
                default:
                    // Anything above command is arguments.
                    command.add(part);
                }
                ++pos;
            }
        }
    }

    public int getUid() {
        return uid;
    }

    public int getPid() {
        return pid;
    }

    public int getParentPid() {
        return parentPid;
    }

    public int getRecentCpuUsage() {
        return recentCpuUsage;
    }

    public Date getStartTime() {
        return startTime;
    }

    public String getTty() {
        return tty;
    }

    public Date getElapsedCpuUsage() {
        return elapsedCpuUsage;
    }

    public ArrayList<String> getCommand() {
        return command;
    }

}
