package org.logger.service;

public interface LogReaderDelegate {
    void append(String lines, Object userData);
}
