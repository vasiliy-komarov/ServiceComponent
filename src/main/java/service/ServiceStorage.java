package service;

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServiceStorage implements Service {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public byte[] get(String key) throws BadKeyException, Exception {
        readLock.lock();

        Optional.ofNullable(key).filter(k -> !k.isEmpty()).orElseThrow(BadKeyException::new);

        try {
            int hashCode = key.hashCode();
        } catch (Exception e) {
            throw e;
        } finally {
            readLock.unlock();
        }
        return new byte[1];
    }

    public void put(String key, byte[] data) throws BadKeyException, Exception {
        writeLock.lock();

        Optional.ofNullable(key).filter(k -> !k.isEmpty()).orElseThrow(BadKeyException::new);

        try {
            int hashCode = key.hashCode();

        } catch (Exception e) {

        } finally {
            writeLock.unlock();
        }
    }

    public void remove(String key) throws BadKeyException, Exception {
        writeLock.lock();

        Optional.ofNullable(key).filter(k -> !k.isEmpty()).orElseThrow(BadKeyException::new);

        try {
            int hashCode = key.hashCode();

        } catch (Exception e) {

        } finally {
            writeLock.unlock();
        }
    }
}
