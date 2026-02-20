package cool.javaee.cdi.events.observers;

import cool.javaee.cdi.events.observers.qualifiers.Important;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Named;

/**
 * A bean with a method listening only to important messages.
 *
 * @author Pavel Pscheidl <pavel.junior@pscheidl.cz>
 */
@Named
@ApplicationScoped
public class ImportantEventObserver {

    @PostConstruct
    public void init() {
        System.out.println("[ImportantEventObserver] initialized and ready to observe @Important qualified String events");
    }

    /**
     * A method observing only events with important messages
     *
     * @param importantMessage Important message
     */
    public void observeSecretMessage(@Observes @Important String importantMessage) {
        System.out.println("[ImportantEventObserver] \"Important message received: " + importantMessage + "\"");
    }

}
