package com.pve.springacctuator;

import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleRestController {
    @GetMapping("/example")
    public String example() {
        return "Hello User !! Time is >>  " + LocalDateTime.now();
    }
}
