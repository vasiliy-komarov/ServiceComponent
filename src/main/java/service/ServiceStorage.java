package service;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.Optional.ofNullable;

public class ServiceStorage implements Service {

//    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
//    private final Lock readLock = readWriteLock.readLock();
//    private final Lock writeLock = readWriteLock.writeLock();

    private HashMap<String, String> keysMap = new HashMap<>();

    public byte[] get(String key) throws WrongKeyException, FileNotFoundException, Exception {
        String newKey = assertKey(key);

        if (!keysMap.containsKey(newKey)) {
            throw new FileNotFoundException();
        }

        return new byte[1];
    }

    public void put(String key, byte[] data) throws WrongKeyException, Exception {
        String newKey = assertKey(key);

        String md5 = DigestUtils.md5Hex(newKey);

        keysMap.put(newKey, md5);
    }

    public void remove(String key) throws WrongKeyException, FileNotFoundException, Exception {
        String newKey = assertKey(key);

        if (!keysMap.containsKey(newKey)) {
            throw new FileNotFoundException();
        }
    }

    private String assertKey(String key) throws WrongKeyException {
        return ofNullable(key)
                .map(String::trim)
                .filter(s -> !s.isEmpty() || s.length() < 129)
                .orElseThrow(WrongKeyException::new);
    }
}
