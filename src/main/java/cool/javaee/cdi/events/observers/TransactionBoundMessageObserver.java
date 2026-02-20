package cool.javaee.cdi.events.observers;

import cool.javaee.cdi.events.observers.qualifiers.Transaction;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Named;

/**
 * Contains method that only listens to transaction-bound messages
 *
 * @author Pavel Pscheidl <pavel.junior@pscheidl.cz>
 */
@Named
@ApplicationScoped
public class TransactionBoundMessageObserver {

    @PostConstruct
    public void init() {
        System.out.println("[TransactionBoundMessageObserver] initialized and ready to observe @Transaction qualified events");
    }

    public void observeBeforeTransactionCompletion(@Observes(during = TransactionPhase.BEFORE_COMPLETION) @Transaction String message) {
        System.out.println("[TransactionBoundMessageObserver] \"Message from within transaction received BEFORE completion: " + message + "\"");
    }

    public void observeAfterTransactionCompletion(@Observes(during = TransactionPhase.AFTER_SUCCESS) @Transaction String message) {
        System.out.println("[TransactionBoundMessageObserver] \"Message from within transaction received AFTER success: " + message + "\"");
    }

}
