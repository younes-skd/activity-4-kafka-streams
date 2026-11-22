package ma.emsi.app;

import org.junit.jupiter.api.Test;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.common.serialization.*;
import java.util.Properties;
import static org.assertj.core.api.Assertions.*;
class PageAnalyticsTest {
 @Test void aggregatesByPageAndIgnoresMalformedEvents(){
  StreamsBuilder builder=new StreamsBuilder();
  new PageAnalytics().pageCounts().apply(builder.stream("input",Consumed.with(Serdes.String(),Serdes.String()))).to("output",Produced.with(Serdes.String(),Serdes.String()));
  Properties p=new Properties();p.put(StreamsConfig.APPLICATION_ID_CONFIG,"test-counts");p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"dummy:9092");p.put(StreamsConfig.STATESTORE_CACHE_MAX_BYTES_CONFIG,0);
  try(TopologyTestDriver driver=new TopologyTestDriver(builder.build(),p)){
   var input=driver.createInputTopic("input",new StringSerializer(),new StringSerializer());var output=driver.createOutputTopic("output",new StringDeserializer(),new StringDeserializer());
   input.pipeInput("a","home");input.pipeInput("b","home");input.pipeInput("c","products");input.pipeInput("d","bad page!");
   var values=output.readKeyValuesToList();assertThat(values).hasSize(3);
   assertThat(values.get(1).key).isEqualTo("home");assertThat(values.get(1).value).contains("\"count\":2");
   assertThat(values.get(2).value).contains("products");
  }
 }
}
