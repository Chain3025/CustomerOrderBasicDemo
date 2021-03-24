package com.georgian.customerorder.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

@Entity
//@Data

public class OrderProductMapper {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderProductId;

  @ManyToOne
  @JoinColumn(name = "order_id")
  @JsonIgnore
  //@JsonBackReference
  private Orders orders;
  private Long productId;
  private Long productQuantity;
  private Long productPrice;

  public Long getOrderProductId() {
    return orderProductId;
  }

  public void setOrderProductId(Long orderProductId) {
    this.orderProductId = orderProductId;
  }

  public Orders getOrders() {
    return orders;
  }

  public void setOrders(Orders orders) {
    this.orders = orders;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public Long getProductQuantity() {
    return productQuantity;
  }

  public void setProductQuantity(Long productQuantity) {
    this.productQuantity = productQuantity;
  }

  public Long getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(Long productPrice) {
    this.productPrice = productPrice;
  }
}
