package ma.emsi.app;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
@RestController
public class Dashboard {
 private final Map<String,Long> counts=new ConcurrentHashMap<>();
 private final List<SseEmitter> emitters=new CopyOnWriteArrayList<>();private final ObjectMapper json;
 public Dashboard(ObjectMapper j){json=j;}
 @Bean Consumer<String> display(){return payload->{try{var n=json.readTree(payload);counts.put(n.get("page").asText(),n.get("count").asLong());for(var e:emitters)send(e);}catch(Exception e){throw new IllegalArgumentException("Invalid analytics event",e);}};}
 @GetMapping("/api/stats") public Map<String,Long> stats(){return new TreeMap<>(counts);}
 @GetMapping(value="/api/stream",produces="text/event-stream") public SseEmitter stream(){
  SseEmitter e=new SseEmitter(60000L);emitters.add(e);e.onCompletion(()->emitters.remove(e));e.onTimeout(()->emitters.remove(e));e.onError(t->emitters.remove(e));send(e);return e;
 }
 private void send(SseEmitter e){try{e.send(SseEmitter.event().name("counts").data(stats()));}catch(Exception ex){emitters.remove(e);e.complete();}}
}
