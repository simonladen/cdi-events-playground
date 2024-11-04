package cool.javaee.cdi.events.observers.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.io.Serializable;

/**
 * A database entity serves as a wrapper for the message to be persisted into
 * database.
 *
 * @author Pavel Pscheidl <pavel.junior@pscheidl.cz>
 */
@Entity
public class Message implements Serializable {

    public Message() {
    }

    public Message(String message) {
        this.message = message;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntity() {
        return message;
    }

    public void setEntity(String entity) {
        this.message = entity;
    }

}
