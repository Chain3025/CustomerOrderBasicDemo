package com.georgian.customerorder.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class OrderProductMapper {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderProductId;
  @ManyToOne()
  @JoinColumn(name = "order_id")
  @JsonBackReference
  private Orders orders;
  private Long productId;
  private Long productQuantity;
  private Long productPrice;

}
