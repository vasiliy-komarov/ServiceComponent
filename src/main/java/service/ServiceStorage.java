package service;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

public class ServiceStorage implements Service {

//    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
//    private final Lock readLock = readWriteLock.readLock();
//    private final Lock writeLock = readWriteLock.writeLock();

    private static Map<String, FileEntry> keysMap = new ConcurrentHashMap<>(); // key = md5 code
//    private static Map<String, FileEntry> keysMap = Collections.synchronizedMap(new HashMap<>()); // key = md5 code
    private String _defaultDir = "../storage/";

    public ServiceStorage() {
        makeDirIfNotExist();
    }

    public ServiceStorage(String defaultDir) {
        _defaultDir = ofNullable(defaultDir).map(String::trim).filter(s -> !s.isEmpty()).orElse(_defaultDir);
        makeDirIfNotExist();
    }

    private void makeDirIfNotExist() {
        File file = new File(_defaultDir);

        if (!file.isDirectory()) {
            file.mkdir();
        }
    }

    private String assertKey(String key) throws WrongKeyException {
        return ofNullable(key)
                .map(String::trim)
                .filter(s -> !s.isEmpty() || s.length() < 129)
                .orElseThrow(WrongKeyException::new);
    }

    private String getEncodedKey(String key) {
        return DigestUtils.md5Hex(key);
    }

    private FileEntry getFileEntry(String key) throws WrongKeyException, WrongDirNameException {
        String assertedKey = assertKey(key);
        String encodedKey = getEncodedKey(assertedKey);

        if (keysMap.containsKey(encodedKey)) {
            System.out.println("getFileEntry contains key = " + key);

            return keysMap.get(encodedKey);
        } else {
            FileEntry fileEntry = new FileEntry(assertedKey, _defaultDir);

            System.out.println("getFileEntry create file entry and put in map");

            keysMap.keySet().forEach(s -> System.out.println("keys in map = " + s));
            keysMap.put(encodedKey, fileEntry);

            return fileEntry;
        }
    }

    public byte[] get(String key) throws WrongKeyException, WrongDirNameException, FileNotFoundException, IOException {
        System.out.println("Service storage, try to get file, thread = " + Thread.currentThread().getName());
        FileEntry fileEntry = getFileEntry(key);
        File file = fileEntry.getFileIfExist();

        if (file == null) throw new FileNotFoundException();

        byte[] fileBytes = Files.readAllBytes(file.toPath());

        return fileBytes;
    }

    public void put(String key, byte[] data) throws WrongKeyException, Exception {
        FileEntry fileEntry = getFileEntry(key);

        fileEntry.writeToFile(data);
    }

    public void remove(String key) throws WrongKeyException, Exception {
        FileEntry fileEntry = getFileEntry(key);

        fileEntry.removeFileIfExist();
    }

    public Map<String, byte[]> getKeyValue() {
        Map<String, byte[]> map = new HashMap<>();

        // find all file names and keys

        return map;
    }

}
