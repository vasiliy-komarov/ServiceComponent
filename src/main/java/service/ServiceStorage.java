package service;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class ServiceStorage implements Service {

//    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
//    private final Lock readLock = readWriteLock.readLock();
//    private final Lock writeLock = readWriteLock.writeLock();

    private Map<String, FileEntry> keysMap = new HashMap<>(); // key = md5 code

    public byte[] get(String key) throws WrongKeyException, FileNotFoundException, Exception {
        String newKey = assertKey(key);
        FileEntry fileEntry;

        if (keysMap.containsKey(newKey)) {
            fileEntry = keysMap.get(newKey);
        } else {
            fileEntry = new FileEntry(newKey, "somedir");
            keysMap.put(newKey, fileEntry);
        }

        File file = fileEntry.getFileIfExist();

        if (file == null) throw new FileNotFoundException();

        byte[] fileBytes = Files.readAllBytes(file.toPath());

        return fileBytes;
    }

    public void put(String key, byte[] data) throws WrongKeyException, Exception {
        String newKey = assertKey(key);

    }

    public void remove(String key) throws WrongKeyException, Exception {
        String newKey = assertKey(key);

    }

    private String assertKey(String key) throws WrongKeyException {
        return ofNullable(key)
                .map(String::trim)
                .filter(s -> !s.isEmpty() || s.length() < 129)
                .map(DigestUtils::md5Hex)
                .orElseThrow(WrongKeyException::new);
    }

    public Map<String, String> getKeyValue() {
        Map<String, String> map = new HashMap<>();

        // find all file names and keys

        return map;
    }

}
