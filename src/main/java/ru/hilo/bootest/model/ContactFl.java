package ru.hilo.bootest.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "contactfls")
public class ContactFl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String flId;
    private String statusFl;
    private String type;
    private String applicationId;
    private String applicationStatus;
    private String flAddDate;
    private Boolean lock;
}
