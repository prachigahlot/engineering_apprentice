package lab;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationStateInspectorTest {

    @Test
    void inspectsIntegerTransition() {
        var result = ConfigurationStateInspector.inspect(
                "retries", "int", "3", "4");

        assertEquals("retries", result.name());
        assertEquals(3, result.before());
        assertEquals(4, result.after());
        assertEquals(4, result.returnedValue());
    }

    @Test
    void acceptsIntegerBoundaries() {
        assertEquals(0,
                ConfigurationStateInspector.inspect(
                        "retries", "int", "0", "0").after());

        assertEquals(100,
                ConfigurationStateInspector.inspect(
                        "retries", "int", "100", "100").after());
    }

    @Test
    void rejectsEmptyName() {
        assertThrows(IllegalArgumentException.class,
                () -> ConfigurationStateInspector.inspect(
                        " ", "int", "3", "4"));
    }

    @Test
    void rejectsInvalidInteger() {
        assertThrows(IllegalArgumentException.class,
                () -> ConfigurationStateInspector.inspect(
                        "retries", "int", "abc", "4"));
    }

    @Test
    void rejectsIntegerAboveRange() {
        assertThrows(IllegalArgumentException.class,
                () -> ConfigurationStateInspector.inspect(
                        "retries", "int", "3", "101"));
    }

    @Test
    void rejectsUnsupportedType() {
        assertThrows(IllegalArgumentException.class,
                () -> ConfigurationStateInspector.inspect(
                        "enabled", "float", "1", "2"));
    }

    @Test
    void acceptsBooleanTransition() {
        var result = ConfigurationStateInspector.inspect(
                "enabled", "boolean", "false", "true");

        assertEquals(false, result.before());
        assertEquals(true, result.after());
    }
}
