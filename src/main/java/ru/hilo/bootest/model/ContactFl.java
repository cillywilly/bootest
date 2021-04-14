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
    private String  fl_id;
    private String  status_fl;
    private String  type;
    private String  application_id;
    private String  application_status;
    private String  fl_add_date;
    private Boolean lock;
}
