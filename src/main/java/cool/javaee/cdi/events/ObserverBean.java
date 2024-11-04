package cool.javaee.cdi.events;

import fish.payara.micro.cdi.Inbound;
import fish.payara.notification.eventbus.EventbusMessage;
import fish.payara.notification.healthcheck.HealthCheckNotificationData;
import fish.payara.notification.healthcheck.HealthCheckResultEntry;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Named;
import java.util.Optional;


/**
 *
 * @author Simon Laden
 */
@Dependent
public class ObserverBean{

    public void observe(@Observes @Inbound EventbusMessage event) {
        var shortInfo = event.getSubject();
        var detailedMessage = event.getMessage();

        var domainName = event.getDomain();
        var sourceInstanceName = event.getInstance();

        System.out.println("domain = " + domainName + " - source = " + sourceInstanceName + " - short = " + shortInfo + " - details = " + detailedMessage);
        if (event.getData() instanceof HealthCheckNotificationData) {
            Optional<HealthCheckResultEntry> mostCritical = event.getData()
            .as(HealthCheckNotificationData.class).getEntries()
            .stream().sorted().findFirst();
        }
    }
}
