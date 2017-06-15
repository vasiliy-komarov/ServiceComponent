package service;

public interface Service {
    byte[] get(String key) throws Exception;
    void put(String key, byte[] data) throws Exception;
    void remove(String key) throws Exception;
}
