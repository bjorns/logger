package org.logger.service;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.logging.Logger;

public class LogReader implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(LogReader.class.getName());
    private final File logfile;
    private boolean isRunning = true;
    private static final int MILLISECONDS = 1;
    private static final int INTERVAL = 100 * MILLISECONDS;
    private long filePointer;
    private final LogReaderDelegate delegate;
    private Object userData = null;

    public LogReader(String logname, LogReaderDelegate delegate, Object userData) {
        this.logfile = new File(logname);
        if (logfile != null) {
            this.filePointer = logfile.length();
        }
        this.userData = userData;
        this.delegate = delegate;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                Thread.sleep(INTERVAL);
                long len = logfile.length();
                if (len < filePointer) {
                    filePointer = len;
                } else if (len > filePointer) {
                    // File must have had something added to it!
                    RandomAccessFile raf = new RandomAccessFile(logfile, "r");
                    raf.seek(filePointer);
                    String line = null;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = raf.readLine()) != null) {
                        stringBuilder.append(line);
                        stringBuilder.append("\n");
                    }
                    delegate.append(stringBuilder.toString(), userData);
                    filePointer = raf.getFilePointer();
                    raf.close();
                }
            }
        } catch (Exception e) {
            LOGGER.warning("Fatal error reading log file " + logfile.getName() + " log tailing has stopped.");
        }

    }

    public void stopRunning() {
        isRunning = false;
    }

    public void setIsRunning(boolean value) {
        isRunning = value;
    }

}
