package com.georgian.customerorder.demo.repository;

import com.georgian.customerorder.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

  @Query(value = "SELECT count(*) FROM Orders WHERE customerId = :customerId")
  Long countByCustomerId(@Param("customerId") Long customerId);


}
