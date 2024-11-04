package cool.javaee.cdi.events;

import cool.javaee.cdi.events.database.MessageSaver;
import cool.javaee.cdi.events.observers.qualifiers.Important;
import jakarta.enterprise.context.Dependent;
import java.io.Serializable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.event.Event;

/**
 *
 * @author Pavel Pscheidl <pavel.junior@pscheidl.cz>
 */
@Dependent
public class EventViewBean implements Serializable {

    @Inject
    private Event<String> simpleMessageEvent;

    @Inject
    @Important
    private Event<String> importantMessageEvent;

    @Inject
    private MessageSaver messageSaver;

    /**
     * Fires simple event with the message received from the website frontend.
     * Event listeners without any additional qualifiers will receive this
     * event.
     *
     * @param message Message to send
     */
    public void sendSimpleMessage(String message) {
        simpleMessageEvent.fire(message);
    }

    /**
     * Fires simple event with the message received from the website frontend.
     * Event listeners without any additional qualifiers will receive this
     * event.
     *
     * @param messageMessage to send
     */
    public void sendImportantMessage(String message) {
        importantMessageEvent.fire(message);

    }

    /**
     * Fires simple event with the message received from the website frontend.
     * Event listeners without any additional qualifiers will receive this
     * event.
     *
     * @param message Message to send
     */
    public void sendImportantMessageAlternatively(String message) {
        simpleMessageEvent.select(new AnnotationLiteral<Important>() {
        })
                .fire(message);

    }

    /**
     * Persists a message into database. Inside the saveMessageToDatabase
     * method, an event is fired.
     *
     * @param message Message to send
     */
    public void sendMessageToTransactionEvent(String message) {
        messageSaver.saveMessageToDatabase(message);
    }

}
