package com.georgian.customerorder.demo.repository;

import com.georgian.customerorder.demo.entity.OrderProductMapper;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderProductMapperRespository extends JpaRepository<OrderProductMapper,Long> {

  }
