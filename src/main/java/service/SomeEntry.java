package service;

import org.apache.commons.codec.digest.DigestUtils;

public class SomeEntry {

    private String _key;
    private String _fileName;
    private byte[] _value;

    public SomeEntry(String key, byte[] value) {
        _value = value;
        _key = key;
        _fileName = DigestUtils.md5Hex(key);
    }

    public String getKey() {
        return _key;
    }

    public byte[] getValue() {
        return _value;
    }

    public void setKey(String key) {
        _key = key;
    }

    public void setValue(byte[] value) {
        _value = value;
    }

    public void writeToFile() {
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
