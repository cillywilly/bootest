package ru.hilo.bootest.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.hilo.bootest.model.ContactFl;
import ru.hilo.bootest.services.ContactFlsServise;

@RestController
@RequiredArgsConstructor
public class SomeController {

    private final ContactFlsServise contactFlsServise;

    @GetMapping("/hello/{id}")
    public ContactFl hello(@PathVariable Integer id) {
        return contactFlsServise.getContactFl(id);
    }

    @GetMapping("/hello")
    public String hellos() {
        return "hello beach";
    }
}
