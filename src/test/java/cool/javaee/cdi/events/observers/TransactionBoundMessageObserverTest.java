package cool.javaee.cdi.events.observers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TransactionBoundMessageObserverTest {

    private final TransactionBoundMessageObserver observer = new TransactionBoundMessageObserver();

    @Test
    void initLogsReadyMessage() {
        assertDoesNotThrow(observer::init);
    }

    @Test
    void observeBeforeTransactionCompletionHandlesMessage() {
        assertDoesNotThrow(() -> observer.observeBeforeTransactionCompletion("pre-commit"));
    }

    @Test
    void observeAfterTransactionCompletionHandlesMessage() {
        assertDoesNotThrow(() -> observer.observeAfterTransactionCompletion("post-commit"));
    }
}
