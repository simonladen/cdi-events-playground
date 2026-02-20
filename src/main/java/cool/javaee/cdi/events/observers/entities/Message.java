package cool.javaee.cdi.events.observers.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @javax.persistence.TableGenerator(name = "MSG_GEN", table = "SEQUENCE_TABLE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MSG_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "MSG_GEN")
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
