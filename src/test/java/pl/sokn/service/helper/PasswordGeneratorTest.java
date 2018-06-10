package pl.sokn.service.helper;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordGeneratorTest {

    @Test
    public void generatePassword() {
        int length = 10;

        final String pass = PasswordGenerator.get(10);

        assertNotNull(pass);
        assertEquals(length, pass.length());
    }
}