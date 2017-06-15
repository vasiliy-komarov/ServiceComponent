package service;

public class SomeEntry {

    private String _key;
    private String md5Key;
    private byte[] _value;

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
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String getMd5Key() {
        return md5Key;
    }

}
