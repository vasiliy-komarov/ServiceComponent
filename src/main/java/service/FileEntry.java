package service;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private void write(File file, byte[] value) {
        AtomicReference<File> atom = new AtomicReference<>(file);

        atom.updateAndGet(current -> {
            System.out.println("UPDATE AND GET, current = " + current.getAbsolutePath());

            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(value);

                System.out.println("WRITE SUCCESS!");

                String newName = file.getAbsolutePath().replace(".swap", "");
                boolean isRenamed = file.renameTo(new File(newName));

                System.out.println("RENAME FILE, file = " + file.getAbsolutePath() + ", isRenamed = " + isRenamed);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return current;
        });
    }

    public void writeToFile(byte[] value) throws Exception {
        writeLock.lock();

        File file = getFileIfExist();
        try {
            System.out.println("WRITE LOCK WRITE FILE, f = " + file);
            String filePath = _dir + _fileName + ".swap";

            File newFile = new File(filePath);
            boolean isCreated = newFile.createNewFile();

            if (isCreated) {
                if (file != null) {
                    write(newFile, value);
                    file.delete();
                } else {
                    write(newFile, value);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            writeLock.unlock();
            System.out.println("WRITE UNLOCK WRITE FILE, f = " + file);
        }

    }

    public File getFileIfExist() {
        readLock.lock();
        String threadName = Thread.currentThread().getName();
        System.out.println("getFileIfExist READLOCK FileEntry, thread name = " + threadName
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
            System.out.println("getFileIfExist READUNLOCK FileEntry, thread name = " + Thread.currentThread().getName());
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
