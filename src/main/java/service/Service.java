package service;

import java.io.IOException;

public interface Service {
    byte[] get(String key) throws WrongKeyException, FileNotFoundException, WrongDirNameException, IOException;
    void put(String key, byte[] data) throws Exception;
    void remove(String key) throws Exception;
}
