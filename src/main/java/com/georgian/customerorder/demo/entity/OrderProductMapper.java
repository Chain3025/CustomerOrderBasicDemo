package com.georgian.customerorder.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class OrderProductMapper {

  @Id
  private Long orderProductId;
  private Long orderId;
  private Long productId;
  private Long productQuantity;

}
