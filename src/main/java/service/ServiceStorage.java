package service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.Optional.ofNullable;

public class ServiceStorage implements Service {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public byte[] get(String key) throws WrongKeyException, Exception {
        String newKey = ofNullable(key).map(String::trim).filter(s -> !s.isEmpty() && s.length() < 129).orElseThrow(WrongKeyException::new);

        readLock.lock();
        try {
            int hashCode = newKey.hashCode();
        } catch (Exception e) {
            throw e;
        } finally {
            readLock.unlock();
        }
        return new byte[1];
    }

    public void put(String key, byte[] data) throws WrongKeyException, Exception {
        writeLock.lock();

        try {
            int hashCode = key.hashCode();

        } catch (Exception e) {

        } finally {
            writeLock.unlock();
        }
    }

    public void remove(String key) throws WrongKeyException, Exception {
        writeLock.lock();

        try {
            int hashCode = key.hashCode();

        } catch (Exception e) {

        } finally {
            writeLock.unlock();
        }
    }
}
