package com.georgian.customerorder.demo.service;

import com.georgian.customerorder.demo.entity.OrderProductMapper;
import com.georgian.customerorder.demo.entity.Product;
import com.georgian.customerorder.demo.repository.OrderProductMapperRespository;
import com.georgian.customerorder.demo.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderProductMapperService {

  @Autowired
  private final OrderProductMapperRespository orderProductMapperRespository;
  private final ProductRepository productRepository;
  public OrderProductMapperService(OrderProductMapperRespository orderProductMapperRespository,
      ProductRepository productRepository) {
    this.orderProductMapperRespository = orderProductMapperRespository;
    this.productRepository = productRepository;

  }


  public ResponseEntity<OrderProductMapper> updateOrderProductMapper(OrderProductMapper reqOrderProductMapper,
      Long orderProductId) {
    //fetch orderproductmapper by id for check as  it is present or not
    Optional<OrderProductMapper> byId = orderProductMapperRespository.findById(orderProductId);
    if(!byId.isPresent()){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    //present then update its product quantity as per reqOrderProductMapper
    OrderProductMapper orderProductMapper = byId.get();
    // update product id in mapper
    if(reqOrderProductMapper.getProductId()!=null)
      orderProductMapper.setProductId(reqOrderProductMapper.getProductId());
    // update product quantity in mapper table
    if(reqOrderProductMapper.getProductQuantity()!=null)
      orderProductMapper.setProductQuantity(reqOrderProductMapper.getProductQuantity());

    OrderProductMapper savedOrderProductMapper = orderProductMapperRespository.save(orderProductMapper);
    return new ResponseEntity<>(savedOrderProductMapper,HttpStatus.ACCEPTED);
  }


  public ResponseEntity<List<Product>> retierveAllProducts() {
    List<Product> all = productRepository.findAll();
    if(all.size()<=0)
      return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
    return new ResponseEntity<>(all,HttpStatus.OK);
  }

  public ResponseEntity<HttpStatus> deleteOrderProductMapperById(Long orderProductId) {
    Optional<OrderProductMapper> byId = orderProductMapperRespository.findById(orderProductId);
    if(!byId.isPresent())
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    orderProductMapperRespository.delete(byId.get());
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  public void addOrderProductMapper(OrderProductMapper orderProductMapper) {
    orderProductMapperRespository.save(orderProductMapper);
  }
}
