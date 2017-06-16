package service;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileEntry {

    private String _key;
    private String _dir = "";
    private String _fileName;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public FileEntry(String key, String dir) throws WrongKeyException, WrongDirNameException {
        Optional.ofNullable(key).map(String::trim).filter(k -> !k.isEmpty()).orElseThrow(WrongKeyException::new);
        Optional.ofNullable(dir).map(String::trim).filter(d -> !d.isEmpty()).orElseThrow(WrongDirNameException::new);

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
//        File f = getFileIfExist();

    }

    public File getFileIfExist() {
        readLock.lock();
        System.out.println("LOCK FileEntry, thread name = " + Thread.currentThread().getName());
        try {
            File[] listFiles = new File(_dir).listFiles((f, name) -> Objects.equals(_fileName, name));
            File f = null;

            if (listFiles != null) {
                for (File file : listFiles) {
                    if (file != null) {
                        f = file;
                        break;
                    }
                }
            }

            return f;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        return null;
    }

    private boolean isExistFile() {
        String[] listFiles = new File(_dir).list((f, name) -> Objects.equals(_fileName, name));

        return listFiles != null && listFiles.length > 0;
    }

    public void removeFileIfExist() {

    }
}
