package service;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileEntry {

    private String _key;
    private String _dir;
    private Integer _fileName;
//    private String _fileName;
//    private File _file;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public FileEntry(String key, String dir) throws WrongKeyException, WrongDirNameException {
        _key = Optional.ofNullable(key).map(String::trim).filter(k -> !k.isEmpty()).orElseThrow(WrongKeyException::new);
        _dir = Optional.ofNullable(dir)
                .map(String::trim)
                .filter(d -> !d.isEmpty())
//                .map(d -> d += d.endsWith("/") || d.endsWith("\\") ? "" : "/")
                .orElseThrow(WrongDirNameException::new);
//        _fileName = DigestUtils.md5Hex(key);
        _fileName = key.hashCode();
        System.out.println("CONSTRUCTOR FILE ENTRY, hashCode = " + _fileName);

//        _file = getFileIfExist();
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
            System.out.println("UPDATE AND GET, current = " + current.getAbsolutePath());

            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(value);
                System.out.println("WRITE SUCCESS!");

            } catch (IOException e) {
                e.printStackTrace();
                file.delete();
            }

            if (file.exists()) {
                String newName = _dir + _fileName;
                System.out.println("newName = " + newName);

                try {
                    Files.move(file.toPath(), new File(newName).toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    file.delete();
                    System.out.println("Error while rename file = " + file.getName());
                    e.printStackTrace();
                }
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
                System.out.println("old file path = " + file.getAbsolutePath());
                boolean isDeleted = file.delete();
                System.out.println("old file was deleted = " + isDeleted);
            }
            System.out.println("WRITE LOCK WRITE FILE, f = " + file + ", thread name = " + Thread.currentThread().getName());
//            File tempFile = File.createTempFile("", ".swap", new File()_dir);
            File newFile = new File(filePath);
            boolean isCreated = newFile.createNewFile();

            if (isCreated) {
                write(newFile, value);
                System.out.println("new file path = " + newFile.getAbsolutePath());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            writeLock.unlock();
            System.out.println("WRITE UNLOCK WRITE FILE");
        }

    }

    public File getFile() {
        readLock.lock();
        try {
            String threadName = Thread.currentThread().getName();
            System.out.println("getFile READLOCK FileEntry, thread name = " + threadName
                    + ", name file = " + _key
            );
            return getFileIfExist();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("getFile READUNLOCK FileEntry, thread name = " + Thread.currentThread().getName());
            readLock.unlock();
        }
        return null;
    }

    private File getFileIfExist() {
        try {
            File existFile = null;
//            String tempName = _fileName + ".swap";
            File[] listFiles = new File(_dir).listFiles((f, name) -> {
                System.out.println("fileName = " + name + ", _file = " + _fileName + ", thread name = " +Thread.currentThread().getName());

                Integer hashCode = null;
                try {
                    hashCode = Integer.parseInt(name);
                } catch (NumberFormatException e) {
                    System.out.println("Can't parse int name, message = " + e.getMessage());
                }
                return Objects.equals(_fileName, hashCode);
//                return Objects.equals(_fileName, name) || Objects.equals(tempName, name);
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
//            File[] listFiles = new File(_dir).listFiles((f, name) -> Objects.equals(_fileName, name));
//
//            if (listFiles != null) {
//                for (File file : listFiles) {
//                    if (file != null) {
//                        existFile = file;
//                        break;
//                    }
//                }
//            }

            return Optional.ofNullable(existFile).map(File::delete).orElse(false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
        return false;
    }
}
