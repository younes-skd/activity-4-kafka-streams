package ma.emsi.app;

import org.springframework.context.annotation.*;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.*;
@Configuration
public class PageConsumer {
 private static final Logger LOG=LoggerFactory.getLogger(PageConsumer.class);
 private final AtomicLong count=new AtomicLong();
 @Bean Consumer<String> consume(){return page->LOG.info("Received page={} sequence={}",page,count.incrementAndGet());}
}
