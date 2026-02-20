package cool.javaee.cdi.events;

import cool.javaee.cdi.events.database.MessageSaver;
import cool.javaee.cdi.events.observers.qualifiers.Important;
import javax.faces.view.ViewScoped;
import javax.enterprise.event.Event;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 * @author Pavel Pscheidl <pavel.junior@pscheidl.cz>
 */
@Named
@ViewScoped
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
        System.out.println("[EventViewBean] Firing simple unqualified event: " + message);
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
        System.out.println("[EventViewBean] Firing @Important qualified event: " + message);
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
        System.out.println("[EventViewBean] Firing @Important qualified event (alternative selection): " + message);
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
        System.out.println("[EventViewBean] Firing @Transaction qualified event via MessageSaver: " + message);
        messageSaver.saveMessageToDatabase(message);
    }

}
