package ru.hilo.bootest.Things;

import ru.hilo.bootest.model.ContactFl;

public class GeneratorApp {

    public ContactFl createFL (String flType) {
        ContactFl contactFl = new ContactFl();
        //или просто, ИД идет в ТДСЕ и генерит в зависимотри от типа ФЛа
        return new ContactFl();
    }

    public void genAppOnFl(ContactFl contactFl) {
        //генерит УЗ на клиента
    }
}
