package cool.javaee.cdi.events.observers.qualifiers;

import jakarta.inject.Qualifier;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class QualifierAnnotationsTest {

    private static final ElementType[] EXPECTED_TARGETS = {
            ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE
    };

    @Test
    void importantIsARuntimeCdiQualifier() {
        assertNotNull(Important.class.getAnnotation(Qualifier.class));
        assertEquals(RetentionPolicy.RUNTIME, Important.class.getAnnotation(Retention.class).value());
        assertArrayEquals(EXPECTED_TARGETS, Important.class.getAnnotation(Target.class).value());
    }

    @Test
    void transactionIsARuntimeCdiQualifier() {
        assertNotNull(Transaction.class.getAnnotation(Qualifier.class));
        assertEquals(RetentionPolicy.RUNTIME, Transaction.class.getAnnotation(Retention.class).value());
        assertArrayEquals(EXPECTED_TARGETS, Transaction.class.getAnnotation(Target.class).value());
    }
}
