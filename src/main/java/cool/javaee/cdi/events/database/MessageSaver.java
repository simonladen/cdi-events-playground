package cool.javaee.cdi.events.database;

import cool.javaee.cdi.events.observers.entities.Message;
import cool.javaee.cdi.events.observers.qualifiers.Transaction;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author Pavel Pscheidl <pavel.junior@pscheidl.cz>
 */
@Stateless
public class MessageSaver {

    @PersistenceContext
    private EntityManager em;

    @Inject
    @Transaction
    private Event<String> transactionMessageEvent;

    /**
     * Persists message to database and fires an event with the message. The
     * receiver may decide whether to receive the event before or after the
     * transaction was ended and the message was persisted.
     *
     * @param message Message to persist
     */
    public void saveMessageToDatabase(String message) {
        transactionMessageEvent.fire(message);
        System.out.println("Beginning persistence of message: " + message);
        em.persist(new Message(message));
        System.out.println("Message persisted.");
    }

}
