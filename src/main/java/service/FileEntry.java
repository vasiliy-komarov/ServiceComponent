package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.Optional.ofNullable;

public class FileEntry {

    private String _key;
    private String _dir;
    private Integer _fileName;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public FileEntry(String key, String dir) throws WrongKeyException, WrongDirNameException {
        _key = ofNullable(key).map(String::trim).filter(k -> !k.isEmpty()).orElseThrow(WrongKeyException::new);
        _dir = ofNullable(dir)
                .map(String::trim)
                .filter(d -> !d.isEmpty())
                .orElseThrow(WrongDirNameException::new);
        _fileName = key.hashCode();
    }

    public String getKey() {
        return _key;
    }

    public String getFileName() {
        return _fileName.toString();
    }

    private void write(File file, byte[] value) {
        AtomicReference<File> atom = new AtomicReference<>(file);

        atom.updateAndGet(current -> {

            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(value);
                out.close();

                String newName = _dir + _fileName;

                Files.move(file.toPath(), new File(newName).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                file.delete();
            }

            return current;
        });
    }

    public void writeToFile(byte[] value) throws Exception {
        writeLock.lock();
        try {
            String filePath = _dir + _fileName + ".swap";

            File file = getFileIfExist();
            if (file != null) {
                file.delete();
            }

            File newFile = new File(filePath);
            boolean isCreated = newFile.createNewFile();

            if (isCreated) {
                write(newFile, value);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            writeLock.unlock();
        }

    }

    public File getFile() {
        readLock.lock();
        try {
            return getFileIfExist();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        return null;
    }

    private File getFileIfExist() {
        try {
            File existFile = null;
            File[] listFiles = new File(_dir).listFiles((f, name) -> {

                Integer hashCode = null;
                try {
                    hashCode = Integer.parseInt(name);
                } catch (NumberFormatException e) {
                }
                return Objects.equals(_fileName, hashCode);
            });

            if (listFiles != null) {
                for (File file : listFiles) {
                    if (file != null) {
                        existFile = file;
                        break;
                    }
                }
            }
            return existFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean removeFileIfExist() {
        writeLock.lock();

        try {
            File existFile = getFileIfExist();
            return ofNullable(existFile).map(File::delete).orElse(false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
        return false;
    }
}
