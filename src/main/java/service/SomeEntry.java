package service;

import org.apache.commons.codec.digest.DigestUtils;

public class SomeEntry {

    private String _key;
    private String _fileName;

    public SomeEntry(String key) {
        _key = key;
        _fileName = DigestUtils.md5Hex(key);
    }

    public String getKey() {
        return _key;
    }

    public void writeToFile(byte[] value) {
        // TODO LOCK
    }

    public String getFileName() {
        return _fileName;
    }

    @Override
    public int hashCode() {
        return _fileName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
