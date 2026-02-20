package cool.javaee.cdi.events.observers;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Named;

/**
 * A class containing a method listening to all events of String type
 *
 * @author Pavel Pscheidl <pavel.junior@pscheidl.cz>
 */
@Named
@ApplicationScoped
public class SimpleEventObserver {

    @PostConstruct
    public void init() {
        System.out.println("[SimpleEventObserver] initialized and ready to observe unqualified String events");
    }

    /**
     * Observes all events with String generic type.
     *
     * @param message Message from the event
     */
    private void simplyObserve(@Observes String message) {
        System.out.println("[SimpleEventObserver] \"Simply observed: " + message + "\"");
    }

}
