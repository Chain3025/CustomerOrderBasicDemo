package com.georgian.customerorder.demo.repository;

import com.georgian.customerorder.demo.entity.OrderProductMapper;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderProductMapperRespository extends JpaRepository<OrderProductMapper,Long> {
//  @Query(value = "delete from order_product_mapper where  and not in(Select order_product_id from orderProductMapper where order_id =:orderIdForDelete)",nativeQuery = true)
//  void deleteByOrderIdAndListOfOrderProductId(@Param("orderIdForDelete") Long orderIdForDelete,
//      @Param("orderProductMapper") OrderProductMapper orderProductMapper);
}
