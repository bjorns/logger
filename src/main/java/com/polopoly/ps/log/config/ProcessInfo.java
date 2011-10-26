package com.polopoly.ps.log.config;

import java.util.ArrayList;
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

    private String uid;
    private int pid;
    private int parentPid;
    private int recentCpuUsage;
    private String startTime;
    private String tty;
    private String elapsedCpuUsage;
    private ArrayList<String> command;

    public ProcessInfo(String line) {
        String[] parts = line.split(" ");
        int pos = 0;
        for (String part : parts) {
            if (part != null && !part.equals("")) {
                switch (pos) {
                case UID:
                    uid = part;
                    break;
                case PID:
                    try {
                        pid = Integer.valueOf(part);
                    } catch (NumberFormatException e) {
                        LOGGER.warning("Could not parse pid " + part + " as integer.");
                    }

                    break;
                case PARENT_PID:
                    try {
                        parentPid = Integer.valueOf(part);
                    } catch (NumberFormatException e) {
                        LOGGER.warning("Could not parse parent pid " + part + " as integer.");
                    }

                    break;
                case RECENT_CPU_USAGE:
                    try {
                        recentCpuUsage = Integer.valueOf(part);
                    } catch (NumberFormatException e) {
                    }

                    break;
                case START_TIME:
                    startTime = part;
                    break;
                case TTY:
                    tty = part;
                    break;
                case ELAPSED_CPU_USAGE:
                    elapsedCpuUsage = part;
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

    public String getUid() {
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

    public String getStartTime() {
        return startTime;
    }

    public String getTty() {
        return tty;
    }

    public String getElapsedCpuUsage() {
        return elapsedCpuUsage;
    }

    public ArrayList<String> getCommand() {
        return command;
    }

}
