package service;

import org.apache.commons.codec.digest.DigestUtils;

public class SomeEntry {

    private String _key;
    private String md5Key;
    private byte[] _value;

    public SomeEntry(byte[] value, String key) {
        _value = value;
        _key = key;
        md5Key = DigestUtils.md5Hex(key);
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


    @Override
    public int hashCode() {
        return md5Key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String getMd5Key() {
        return md5Key;
    }

}
