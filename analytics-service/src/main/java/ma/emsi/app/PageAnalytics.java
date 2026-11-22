package ma.emsi.app;

import org.springframework.context.annotation.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.common.serialization.Serdes;
import java.util.function.Function;
@Configuration
public class PageAnalytics {
 @Bean public Function<KStream<String,String>,KStream<String,String>> pageCounts(){
  return input->input.filter((key,page)->page!=null && page.matches("[a-zA-Z0-9_-]{1,60}"))
   .groupBy((key,page)->page,Grouped.with(Serdes.String(),Serdes.String()))
   .count(Materialized.as("page-count-store"))
   .toStream().mapValues((page,count)->"{\"page\":\""+page+"\",\"count\":"+count+"}");
 }
}
