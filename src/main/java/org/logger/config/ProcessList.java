package org.logger.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessList {
    private static final Logger LOGGER = Logger.getLogger(ProcessList.class.getName());
    private List<ProcessInfo> processes;

    public ProcessList(Runtime runtime) {
        String cmds[] = { "ps", "-ef" };

        Process proc;
        try {
            proc = runtime.exec(cmds);
            InputStream inputstream = proc.getInputStream();

            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);

            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            String line;

            // Throw away first line
            bufferedreader.readLine();

            processes = new ArrayList<ProcessInfo>();
            while ((line = bufferedreader.readLine()) != null) {
                processes.add(new ProcessInfo(line));
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not list procs", e);
        }

    }

    Iterator<ProcessInfo> processes() {
        return processes.iterator();

    }
}
