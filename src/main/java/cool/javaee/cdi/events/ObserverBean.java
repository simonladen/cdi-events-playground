package cool.javaee.cdi.events;

import fish.payara.micro.cdi.Inbound;
import fish.payara.notification.eventbus.EventbusMessage;
import fish.payara.notification.healthcheck.HealthCheckNotificationData;
import fish.payara.notification.healthcheck.HealthCheckResultEntry;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Named;
import java.util.Optional;


/**
 *
 * @author Simon Laden
 */
@Dependent
public class ObserverBean{

    public void observe(@Observes @Inbound EventbusMessage event) {
        String shortInfo = event.getSubject();
        Object detailedMessage = event.getMessage();

        String domainName = event.getDomain();
        String sourceInstanceName = event.getInstance();

        System.out.println("domain = " + domainName + " - source = " + sourceInstanceName + " - short = " + shortInfo + " - details = " + detailedMessage);
        if (event.getData() instanceof HealthCheckNotificationData) {
            Optional<HealthCheckResultEntry> mostCritical = event.getData()
            .as(HealthCheckNotificationData.class).getEntries()
            .stream().sorted().findFirst();
        }
        try {
            String entry = String.format("[ObserverBean] domain=%s instance=%s subject=%s message=%s",
                    domainName, sourceInstanceName, shortInfo, detailedMessage);
            EventWebSocketServer.broadcast(entry);
        } catch (Exception ex) {
            System.out.println("[ObserverBean] failed to broadcast inbound event: " + ex.getMessage());
        }
    }
}
