package service;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.ofNullable;

public class ServiceStorage implements Service {

    private static Map<Integer, FileEntry> keysMap = new ConcurrentHashMap<>(); // key = md5 code
    private String _defaultDir = "../storage/";

    private void removeTempFiles() {
        System.out.println("REMOVE TEMP FILES");
        try {
            File[] files = new File(_defaultDir).listFiles((f, n) -> n.endsWith(".swap"));

            System.out.println("THREAD NAME = " + Thread.currentThread().getName());

            AtomicReference<File[]> atom = new AtomicReference<>(files);
            atom.updateAndGet(tempFiles -> {
                System.out.println("UPDATE AND GET, thread = " + Thread.currentThread().getName());
                for (File file : tempFiles) {
                    file.delete();
                }
                return tempFiles;
            });
        } catch (Exception e) {
            System.out.println("Error while try to delete temp files");
            e.printStackTrace();
        }

    }

    public ServiceStorage() {
        makeDirIfNotExist();
        removeTempFiles();
    }

    public ServiceStorage(String defaultDir) {
        _defaultDir = ofNullable(defaultDir).map(String::trim).filter(s -> !s.isEmpty()).orElse(_defaultDir);
        makeDirIfNotExist();
        removeTempFiles();
    }

    private void makeDirIfNotExist() {
        File file = new File(_defaultDir);

        if (!file.isDirectory()) {
            System.out.println("DIR = " + _defaultDir + " was created = " + file.mkdir());
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
        Integer encodedKey = assertedKey.hashCode();//getEncodedKey(assertedKey);

        System.out.println("getFileEntry thread name = " + Thread.currentThread().getName());
        if (keysMap.containsKey(encodedKey)) {
            System.out.println("getFileEntry contains key = " + key);

            return keysMap.get(encodedKey);
        } else {
            FileEntry fileEntry = new FileEntry(assertedKey, _defaultDir);

            System.out.println("getFileEntry create file entry and put in map");

//            keysMap.keySet().forEach(s -> System.out.println("keys in map = " + s));
            keysMap.put(encodedKey, fileEntry);

            return fileEntry;
        }
    }

    public byte[] get(String key) throws WrongKeyException, WrongDirNameException, FileNotFoundException, IOException {
        System.out.println("Service storage, try to get file, thread = " + Thread.currentThread().getName());

        FileEntry fileEntry = getFileEntry(key);
        File file = fileEntry.getFile();

        System.out.println("ServiceStorage, get file = " + file);
        if (file == null) throw new FileNotFoundException();

        System.out.println("Service storage, bytes, name = " + Thread.currentThread().getName());
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        return fileBytes;
    }

    public void put(String key, byte[] data) throws WrongKeyException, Exception {
        FileEntry fileEntry = getFileEntry(key);

        fileEntry.writeToFile(data);
    }

    public boolean remove(String key) throws WrongKeyException, Exception {
        FileEntry fileEntry = getFileEntry(key);

        return fileEntry.removeFileIfExist();
    }

    public Map<String, byte[]> getKeyValue() {
        Map<String, byte[]> map = new HashMap<>();

        // find all file names and keys

        return map;
    }

}
