package ru.hilo.bootest.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SomeController {
    @GetMapping("/hello")
    public String hello() {
        return "hello beach";
    }
}
