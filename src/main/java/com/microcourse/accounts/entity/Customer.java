package com.microcourse.accounts.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
//no se usa data aqui porque no queremos el metodo equals/hascode
//porque puede crear problemas con el FW de JPA
public class Customer extends  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id")
    private Long customerId;

    private String name;

    private String email;

    @Column(name="mobile_number")
    private String mobileNumber;

}