package ma.emsi.app;

import org.springframework.context.annotation.*;
import java.util.function.Supplier;
import java.util.concurrent.ThreadLocalRandom;
@Configuration("supplierConfiguration")
public class PageSupplier {
 @Bean Supplier<String> pageSupplier(){String[] pages={"home","products","checkout"};return ()->pages[ThreadLocalRandom.current().nextInt(pages.length)];}
}
