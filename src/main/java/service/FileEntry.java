package service;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
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
        _key = Optional.ofNullable(key).map(String::trim).filter(k -> !k.isEmpty()).orElseThrow(WrongKeyException::new);
        _dir = Optional.ofNullable(dir)
                .map(String::trim)
                .filter(d -> !d.isEmpty())
                .map(d -> d += d.endsWith("/") ? "" : "/")
                .orElseThrow(WrongDirNameException::new);

        _fileName = DigestUtils.md5Hex(key);
    }

    public String getKey() {
        return _key;
    }

    public String getFileName() {
        return _fileName;
    }

    private void write(File f, byte[] value) {
        AtomicReference<File> atom = new AtomicReference<>(f);

        atom.updateAndGet(current -> {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(f);
                out.write(value);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return current;
        });
    }

    public void writeToFile(byte[] value) throws IOException {
        writeLock.lock();

        File f = getFileIfExist();
        FileOutputStream outputStream = null;
        try {
            if (f != null) {
                write(f, value);
            } else {
                String filePath = new StringBuilder(_dir).append(_dir).append(_fileName).append(".swap").toString();
                File newFile = new File(filePath);
                newFile.createNewFile();
                write(newFile, value);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            writeLock.unlock();
            outputStream.close();
        }

    }

    public File getFileIfExist() {
        readLock.lock();
        String threadName = Thread.currentThread().getName();
        System.out.println("LOCK FileEntry, thread name = " + threadName
                + ", name file = " + _key
        );
        try {
            File[] listFiles = new File(_dir).listFiles((f, name) -> Objects.equals(_fileName, name));
            File f = null;

            if (listFiles != null) {
                System.out.println("SEARCH FILES not empty, name = " + threadName);
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
            System.out.println("UNLOCK FileEntry, thread name = " + Thread.currentThread().getName());
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
