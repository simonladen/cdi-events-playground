package cool.javaee.cdi.events.observers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ImportantEventObserverTest {

    private final ImportantEventObserver observer = new ImportantEventObserver();

    @Test
    void initLogsReadyMessage() {
        assertDoesNotThrow(observer::init);
    }

    @Test
    void observeSecretMessageHandlesImportantMessage() {
        assertDoesNotThrow(() -> observer.observeSecretMessage("classified"));
    }
}
