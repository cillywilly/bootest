package ru.hilo.bootest.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.hilo.bootest.model.ContactFl;

@Repository
public interface ContactFlsRepository extends CrudRepository<ContactFl, Integer> {


}
