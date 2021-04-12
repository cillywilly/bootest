package ru.hilo.bootest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hilo.bootest.model.ContactFl;
import ru.hilo.bootest.repositories.ContactFlsRepository;

@Service
@RequiredArgsConstructor
public class ContactFlsServiceImpl implements ContactFlsServise {

    private ContactFlsRepository contactFlsRepository;

    public ContactFl getContactFl(int id) {
        return contactFlsRepository.findById(id).get();
    }
}
