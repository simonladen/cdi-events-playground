package cool.javaee.cdi.events.observers.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MessageTest {

    @Test
    void noArgConstructorLeavesFieldsUnset() {
        Message message = new Message();

        assertNull(message.getId());
        assertNull(message.getEntity());
    }

    @Test
    void messageConstructorSetsText() {
        Message message = new Message("hello");

        assertEquals("hello", message.getEntity());
        assertNull(message.getId());
    }

    @Test
    void idIsSettableAndGettable() {
        Message message = new Message("hello");

        message.setId(42L);

        assertEquals(42L, message.getId());
    }

    @Test
    void entityIsSettableAndGettable() {
        Message message = new Message();

        message.setEntity("updated");

        assertEquals("updated", message.getEntity());
    }
}
