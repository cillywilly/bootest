package ru.hilo.bootest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hilo.bootest.model.ContactFl;
import ru.hilo.bootest.repositories.ContactFlRepository;

@Service
@RequiredArgsConstructor
public class ContactFlServiceImpl implements ContactFlService {

    private final ContactFlRepository contactFlRepository;

    @Override
    public ContactFl getContactFl(Integer id) {
        return contactFlRepository.findById(id).orElse(new ContactFl());
    }
}
