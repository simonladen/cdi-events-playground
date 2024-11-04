package cool.javaee.cdi.events.observers;

import cool.javaee.cdi.events.observers.qualifiers.Transaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Named;

/**
 * Contains method that only listens to
 *
 * @author Pavel Pscheidl <pavel.junior@pscheidl.cz>
 */
@Named
@ApplicationScoped
public class TransactionBoundMessageObserver {

    public void observeBeforeTransactionCompletion(@Observes(during = TransactionPhase.BEFORE_COMPLETION) @Transaction String message) {
        System.out.println("Message from within transaction received before completion: " + message);
    }

    public void observeAfterTransactionCompletion(@Observes(during = TransactionPhase.AFTER_SUCCESS) @Transaction String message) {
        System.out.println("Message from within transaction received after success: " + message);
    }

}
