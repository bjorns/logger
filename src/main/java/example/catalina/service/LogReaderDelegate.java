package example.catalina.service;

public interface LogReaderDelegate {
    void append(String lines, Object userData);
}
