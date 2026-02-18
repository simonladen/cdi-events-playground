package cool.javaee.cdi.events;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Startup singleton bean to ensure database schema is initialized.
 */
@Singleton
@Startup
public class DDLInitializer {

    @PersistenceContext(unitName = "ExamplePU")
    private EntityManager em;

    @PostConstruct
    public void initialize() {
        try {
            System.out.println("DDL Initializer: Starting schema initialization...");
            
            // Create the MESSAGE table using native SQL
            em.createNativeQuery(
                "CREATE TABLE IF NOT EXISTS MESSAGE (" +
                "  ID BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "  MESSAGE VARCHAR(255)" +
                ")"
            ).executeUpdate();
            // Create a table to back JPA TableGenerator (used for ID generation)
            em.createNativeQuery(
                "CREATE TABLE IF NOT EXISTS SEQUENCE_TABLE (" +
                "  SEQ_NAME VARCHAR(50) PRIMARY KEY, " +
                "  SEQ_COUNT BIGINT" +
                ")"
            ).executeUpdate();

            // Ensure a row exists for our generator key 'MSG_SEQ'
            // Use MERGE which inserts or updates depending on existence (H2 supports MERGE)
            em.createNativeQuery(
                "MERGE INTO SEQUENCE_TABLE (SEQ_NAME, SEQ_COUNT) KEY(SEQ_NAME) VALUES ('MSG_SEQ', 0)"
            ).executeUpdate();
            
            System.out.println("DDL Initializer: MESSAGE table created/verified successfully");
        } catch (Exception e) {
            System.err.println("DDL Initializer: Error creating table - " + e.getMessage());
            e.printStackTrace();
        }
    }
}


