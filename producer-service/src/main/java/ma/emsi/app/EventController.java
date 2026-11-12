package ma.emsi.app;

import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.*;
@RestController
public class EventController {
 private final StreamBridge bridge; public EventController(StreamBridge b){bridge=b;}
 public record Event(String page) {}
 @PostMapping("/events") public ResponseEntity<?> produce(@RequestBody Event event){
  if(event.page()==null || !event.page().matches("[a-zA-Z0-9_-]{1,60}"))return ResponseEntity.badRequest().body("Page must contain 1–60 letters, digits, underscores or hyphens");
  boolean sent=bridge.send("events-out-0",event.page());
  return sent?ResponseEntity.accepted().body(event):ResponseEntity.status(503).body("Broker unavailable");
 }
}
