package service;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SomeEntry {

    private String _key;
    private String _dir;
    private String _fileName;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public SomeEntry(String key, String dir) throws WrongKeyException, WrongDirLocation {
        Optional.ofNullable(key).map(String::trim).filter(k -> !k.isEmpty()).orElseThrow(WrongKeyException::new);
        Optional.ofNullable(dir).map(String::trim).filter(d -> !d.isEmpty()).orElseThrow(WrongDirLocation::new);

        _dir = dir;
        _key = key;
        _fileName = DigestUtils.md5Hex(key);
    }

    public String getKey() {
        return _key;
    }

    public String getFileName() {
        return _fileName;
    }

    public void writeToFile(byte[] value) {
        // TODO LOCK
        File f = getFileIfExist();

    }

    public File getFileIfExist() {
        File[] listFiles = new File(_dir).listFiles((f, name) -> Objects.equals(_fileName, name));
        File f = null;
        for (File file : listFiles) {
            if (file != null) {
                f = file;
                break;
            }
        }
        return f;
    }

    private boolean isExistFile() {
        File[] listFiles = new File(_dir).listFiles((f, name) -> Objects.equals(_fileName, name));

        return listFiles != null && listFiles.length > 0;
    }
}
