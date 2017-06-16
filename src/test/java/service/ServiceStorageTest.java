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
    public void testGetIfKeyLarge() throws Exception {
        ServiceStorage storage = new ServiceStorage();
        thrown.expect(WrongKeyException.class);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 130; i++) {
            builder.append("A");
        }
        storage.get(builder.toString());
    }

    @Test
    public void simpleSum() {
        assertEquals(2,2);
    }

}
