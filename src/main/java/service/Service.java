package service;

import java.io.IOException;
import java.util.Map;

public interface Service {
    byte[] get(String key) throws WrongKeyException, FileNotFoundException, WrongDirNameException, IOException;
    void put(String key, byte[] data) throws Exception;
    boolean remove(String key) throws Exception;
    Map<String, byte[]> getKeyValue();
}
