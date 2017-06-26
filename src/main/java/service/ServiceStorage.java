package service;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class ServiceStorage implements Service {

    private static Map<String, FileEntry> keysMap = new ConcurrentHashMap<>(); // key = md5 code

    private static final String DEFAULT_DIR = "../storage/";
    private static final String PATH_TO_FILE_KEYS = "../storage/keys";

    public static final Long FILES_MAX_COUNT = 20000L;

    public ServiceStorage() {
        synchronized (this) {
            makeDirIfNotExist();
            removeTempFiles();
            makeKeysFile();
            fillKeysMap();
        }
    }

    private void makeKeysFile() {
        File file = new File(PATH_TO_FILE_KEYS);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillKeysMap() {
        try {
            Set<String> setKeys = Files.lines(Paths.get(PATH_TO_FILE_KEYS))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toSet());

            for (String key : setKeys) {
                FileEntry entry = new FileEntry(key, DEFAULT_DIR);
                keysMap.put(key, entry);
            }
        } catch (IOException | WrongKeyException | WrongDirNameException e) {
            e.printStackTrace();
        }
    }

    private void removeTempFiles() {
        try {
            File[] files = new File(DEFAULT_DIR).listFiles((f, n) -> n.endsWith(".swap"));


            AtomicReference<File[]> atom = new AtomicReference<>(files);
            atom.updateAndGet(tempFiles -> {
                for (File file : tempFiles) {
                    file.delete();
                }
                return tempFiles;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void makeDirIfNotExist() {
        File file = new File(DEFAULT_DIR);

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

        if (keysMap.containsKey(assertedKey)) {

            return keysMap.get(assertedKey);
        } else {
            FileEntry fileEntry = new FileEntry(assertedKey, DEFAULT_DIR);

            keysMap.put(assertedKey, fileEntry);

            addNewKey(assertedKey);

            return fileEntry;
        }
    }

    private void addNewKey(String assertedKey) {
        try {
            File keys = new File(PATH_TO_FILE_KEYS);
            AtomicReference<File> atom = new AtomicReference<>(keys);

            boolean isKeyExist = Files.lines(keys.toPath()).anyMatch(s -> s.equals(assertedKey));

            if (!isKeyExist) {
                atom.updateAndGet(cur -> {
                    String key = assertedKey + "\n";
                    try {
                        Files.write(keys.toPath(), key.getBytes(), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return cur;
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] get(String key) throws WrongKeyException, WrongDirNameException, FileNotFoundException, IOException {
        FileEntry fileEntry = getFileEntry(key);
        File file = fileEntry.getFile();

        if (file == null) throw new FileNotFoundException();

        byte[] fileBytes = Files.readAllBytes(file.toPath());

        return fileBytes;
    }

    public void put(String key, byte[] data) throws Exception {
        FileEntry fileEntry = getFileEntry(key);

        fileEntry.writeToFile(data);
    }

    public boolean remove(String key) throws Exception {
        FileEntry fileEntry = getFileEntry(key);

        return fileEntry.removeFileIfExist();
    }

    public Map<String, byte[]> getKeyValue() {
        Map<String, byte[]> map = new HashMap<>();

        for (Map.Entry<String, FileEntry> entry : keysMap.entrySet()) {
            FileEntry fileEntry = entry.getValue();

            try {
                File file = fileEntry.getFile();
                byte[] bytes = Files.readAllBytes(file.toPath());

                map.put(entry.getKey(), bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
    }

}
