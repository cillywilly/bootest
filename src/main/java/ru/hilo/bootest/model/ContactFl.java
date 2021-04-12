package ru.hilo.bootest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "contact_fls")
@NoArgsConstructor
@AllArgsConstructor
public class ContactFl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String  flId;
    private String  statusFl;
    private String  type;
    private String  applicationId;
    private String  applicationStatus;
    private String  flAddDate;
    private Boolean lock;
}
