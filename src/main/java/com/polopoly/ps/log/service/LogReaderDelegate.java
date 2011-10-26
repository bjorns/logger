package com.polopoly.ps.log.service;

public interface LogReaderDelegate {
    void append(String lines, Object userData);
}
