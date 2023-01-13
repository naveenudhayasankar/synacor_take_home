package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// Directs all the requests that are sent to http://localhost:{PORT}/api. The default port is 8080.
@RestController
@RequestMapping("/api")
public class QuotaController {

    // Creates a quota object that is reset to 1000 each time the application is restarted.
    Quota quota = new Quota();

    // Directs post requests with the path /consume.
    // Marshals the value from the JSON request using a POJO (PostRequest)
    @PostMapping("/consume")
    public ResponseEntity<Object> updateRemaining(@RequestBody PostRequest request){
        // If the requested value is greater than the remaining value,
        // send service not available with the excess value requested
        long value = request.getValue();
        if(value > quota.getRemaining()){
            Map<String, Long> excessRequested = new HashMap<>();
            excessRequested.put("excess", value - quota.getRemaining());
            HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
            return new ResponseEntity<>(excessRequested, status);
        }
        // If the requested value is valid, consume value units from remaining,
        // update total consumed units and remaining units.
        else {
            long remaining = quota.getRemaining();
            long consumed = quota.getTotal();
            quota.setRemaining(remaining - value);
            quota.setTotal(consumed + value);
            return ResponseEntity.ok().body(quota);
        }
    }

    // Directs get requests with the path /consume,
    // send the total units consumed and remaining units in the response.
    @GetMapping("/consume")
    public ResponseEntity<Object> getRemaining(){
        return ResponseEntity.ok().body(quota);
    }

    // Directs post requests with the path /reset.
    // Get the new quota value from the request using a POJO (ResetRequest)
    // and set the remaining units to the new quota value and the total consumed units to 0
    @PostMapping("/reset")
    public ResponseEntity<Object> resetQuota(@RequestBody ResetRequest request) {
        quota.setTotal(0);
        quota.setRemaining(request.getQuota());
        return ResponseEntity.ok().body(quota);
    }

}
