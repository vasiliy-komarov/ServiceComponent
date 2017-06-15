package service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class ServiceStorageTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetIfKeyNull() throws Exception {
        ServiceStorage storage = new ServiceStorage();
        thrown.expect(WrongKeyException.class);
        storage.get(null);
    }

    @Test
    public void simpleSum() {
        assertEquals(2,2);
    }

}
