package ma.emsi.app;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.function.Supplier;
import static org.assertj.core.api.Assertions.assertThat;

class SupplierStartupTest {
    @Test void registersFunctionWithoutConfigurationNameCollision() {
        try (var context = new AnnotationConfigApplicationContext(PageSupplier.class)) {
            Supplier<?> supplier = context.getBean("pageSupplier", Supplier.class);
            assertThat(supplier.get()).isIn("home", "products", "checkout");
        }
    }
}
