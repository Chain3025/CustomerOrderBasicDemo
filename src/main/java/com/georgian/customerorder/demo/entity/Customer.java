package com.georgian.customerorder.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String customerName;
    private String customerEmail;
    //@Column(columnDefinition = "varchar(255) default 'Regular'")
    @Enumerated(value = EnumType.ORDINAL)
    private CustomerType customerType;



}
