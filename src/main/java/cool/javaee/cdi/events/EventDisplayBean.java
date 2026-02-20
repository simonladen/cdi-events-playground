package cool.javaee.cdi.events;

import cool.javaee.cdi.events.observers.qualifiers.Important;
import cool.javaee.cdi.events.observers.qualifiers.Transaction;
import fish.payara.micro.cdi.Inbound;
import fish.payara.notification.eventbus.EventbusMessage;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Aggregates observed events so the UI can display them.
 */
@Named
@ApplicationScoped
public class EventDisplayBean implements Serializable {

    private final List<String> messages = Collections.synchronizedList(new LinkedList<>());

    @PostConstruct
    public void init() {
        messages.add("[EventDisplayBean] initialized");
        System.out.println("[EventDisplayBean] initialized");
    }

    public List<String> getMessages() {
        return messages;
    }

    public void clear() {
        messages.clear();
    }

    // Observers for internal CDI events
    public void onSimpleString(@Observes String message) {
        messages.add("[Simple] " + message);
        System.out.println("[EventDisplayBean] Observed simple String: " + message);
        try {
            EventWebSocketServer.broadcast("[Simple] " + message);
        } catch (Exception e) {
            // ignore websocket broadcast failures
        }
    }

    public void onImportantString(@Observes @Important String message) {
        messages.add("[Important] " + message);
        System.out.println("[EventDisplayBean] Observed @Important String: " + message);
        try {
            EventWebSocketServer.broadcast("[Important] " + message);
        } catch (Exception e) {
            // ignore websocket broadcast failures
        }
    }

    public void onTransactionString(@Observes @Transaction String message) {
        messages.add("[Transaction] " + message);
        System.out.println("[EventDisplayBean] Observed @Transaction String: " + message);
        try {
            EventWebSocketServer.broadcast("[Transaction] " + message);
        } catch (Exception e) {
            // ignore websocket broadcast failures
        }
    }

    // Observer for Payara Event Bus messages
    public void onInboundEvent(@Observes @Inbound EventbusMessage event) {
        String entry = String.format("[Inbound] domain=%s instance=%s subject=%s message=%s",
                event.getDomain(), event.getInstance(), event.getSubject(), event.getMessage());
        messages.add(entry);
        System.out.println("[EventDisplayBean] Observed inbound event: " + entry);
        try {
            EventWebSocketServer.broadcast(entry);
        } catch (Exception e) {
            // ignore websocket broadcast failures
        }
    }

    // Fallback observer: try to catch EventbusMessage events without the Inbound qualifier
    public void onAnyEvent(@Observes EventbusMessage event) {
        String entry = String.format("[AnyEvent] domain=%s instance=%s subject=%s message=%s",
                event.getDomain(), event.getInstance(), event.getSubject(), event.getMessage());
        messages.add(entry);
        System.out.println("[EventDisplayBean] Observed any EventbusMessage: " + entry);
        try {
            EventWebSocketServer.broadcast(entry);
        } catch (Exception e) {
            // ignore websocket broadcast failures
        }
    }
}
