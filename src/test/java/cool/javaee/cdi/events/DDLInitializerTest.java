package cool.javaee.cdi.events;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DDLInitializerTest {

    private DDLInitializer ddlInitializer;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @BeforeEach
    void setUp() throws Exception {
        ddlInitializer = new DDLInitializer();
        Field field = DDLInitializer.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(ddlInitializer, entityManager);
    }

    @Test
    void initializeCreatesExpectedSchemaObjects() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);

        ddlInitializer.initialize();

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(entityManager, times(3)).createNativeQuery(sqlCaptor.capture());
        verify(query, times(3)).executeUpdate();

        List<String> statements = sqlCaptor.getAllValues();
        assertTrue(statements.get(0).contains("CREATE TABLE IF NOT EXISTS MESSAGE"));
        assertTrue(statements.get(1).contains("CREATE TABLE IF NOT EXISTS SEQUENCE_TABLE"));
        assertTrue(statements.get(2).contains("MERGE INTO SEQUENCE_TABLE"));
    }

    @Test
    void initializeSwallowsExceptionsWithoutPropagating() {
        when(entityManager.createNativeQuery(anyString())).thenThrow(new RuntimeException("DB unavailable"));

        assertDoesNotThrow(() -> ddlInitializer.initialize());
    }
}
