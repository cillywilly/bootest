package ru.hilo.bootest.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.hilo.bootest.model.ContactFl;
import ru.hilo.bootest.services.ContactFlService;

@RestController
@RequiredArgsConstructor
public class SomeController {

    private final ContactFlService contactFlService;

    @GetMapping("/hello/{id}")
    public ContactFl hello(@PathVariable Integer id) {
        return contactFlService.getContactFl(id);
    }

    @GetMapping("/hello")
    public String hellos() {
        return "hello beach";
    }
}
