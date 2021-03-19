package com.georgian.customerorder.demo.service;

import com.georgian.customerorder.demo.entity.Customer;
import com.georgian.customerorder.demo.entity.CustomerType;
import com.georgian.customerorder.demo.entity.OrderProductMapper;
import com.georgian.customerorder.demo.entity.Orders;
import com.georgian.customerorder.demo.entity.Product;
import com.georgian.customerorder.demo.repository.CustomerRepository;
import com.georgian.customerorder.demo.repository.OrderProductMapperRespository;
import com.georgian.customerorder.demo.repository.OrderRepository;
import com.georgian.customerorder.demo.repository.ProductRepository;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderProductMapperService orderProductMapperService;
    private final OrderProductMapperRespository orderProductMapperRespository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
        ProductRepository productRepository,
        OrderProductMapperService orderProductMapperService,
        OrderProductMapperRespository orderProductMapperRespository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderProductMapperService = orderProductMapperService;
        this.orderProductMapperRespository = orderProductMapperRespository;
    }

    public ResponseEntity<Orders> placeOrder(Orders order) {
        //counts is for check on total no of orders per customers
        //if its <=9 then we have change the customertype and add discount of 10
        Long counts = orderRepository.countByCustomerId(order.getCustomerId());
        //Gets customer through the customerId in order table
        Optional<Customer> byId = customerRepository.findById(order.getCustomerId());
        Customer customer =byId.get();
        //sets its type if condition is meet
        if(counts >= 9) {
            customer.setCustomerType(CustomerType.GOLD);
        }
        if(counts >= 19){
            customer.setCustomerType(CustomerType.PLATINUM);
        }
        customerRepository.save(customer);
        updateDiscountAndTotalPrice(customer,order);
        List<OrderProductMapper> addOrderProductMapperList = order.getOrderProductMapperList();
        for(OrderProductMapper orderProductMapper: addOrderProductMapperList){
            orderProductMapperRespository.save(orderProductMapper);
        }
        Orders save = orderRepository.save(order);
        return new ResponseEntity<Orders>(save, HttpStatus.CREATED);

    }

    private void updateDiscountAndTotalPrice(Customer customer, Orders order) {
        Long totalPrice = null;
        if(customer.getCustomerType().equals(CustomerType.REGULAR)){
             order.setDiscount(0l);
            for(OrderProductMapper orderProductMapper:order.getOrderProductMapperList()){
                Long productId = orderProductMapper.getProductId();
                Optional<Product> byId = productRepository.findById(productId);
                Long productPrice = byId.get().getProductPrice();
                totalPrice += orderProductMapper.getProductQuantity()*productPrice;

            }
            order.setTotalPrice(totalPrice);
        }
        if(customer.getCustomerType().equals(CustomerType.GOLD)){

            for(OrderProductMapper orderProductMapper:order.getOrderProductMapperList()){
                Long productId = orderProductMapper.getProductId();
                Optional<Product> byId = productRepository.findById(productId);
                Long productPrice = byId.get().getProductPrice();
                totalPrice += orderProductMapper.getProductQuantity()*productPrice;

            }
            order.setTotalPrice(totalPrice);
            //Long totalValue = order.getProductPrice()*order.getProductQuantity();
            Long discount = (totalPrice*10)/100;
            order.setDiscount(discount);
        }
        if(customer.getCustomerType().equals(CustomerType.PLATINUM)){

            for(OrderProductMapper orderProductMapper:order.getOrderProductMapperList()){
                Long productId = orderProductMapper.getProductId();
                Optional<Product> byId = productRepository.findById(productId);
                Long productPrice = byId.get().getProductPrice();
                totalPrice += orderProductMapper.getProductQuantity()*productPrice;

            }
            order.setTotalPrice(totalPrice);
            //Long totalValue = order.getProductPrice()*order.getProductQuantity();
            Long discount = (totalPrice*20)/100;
            order.setDiscount(discount);
        }
    }

    public ResponseEntity<List<Orders>> getAllOrder() {
        List<Orders> all = orderRepository.findAll();

        if(all.size()<=0)
            return new ResponseEntity<>(all,HttpStatus.NO_CONTENT);

        return new ResponseEntity<List<Orders>>(all,HttpStatus.OK);
    }

    public ResponseEntity<Orders> deleteOrderById(Long id) {
        Optional<Orders> byId = orderRepository.findById(id);
        if(!byId.isPresent())
            return new ResponseEntity<Orders>(byId.get(),HttpStatus.NOT_FOUND);
        List<OrderProductMapper> orderProductMapperList = byId.get().getOrderProductMapperList();
        for(OrderProductMapper orderProductMapper:orderProductMapperList){
            orderProductMapperRespository.deleteById(orderProductMapper.getOrderId());
        }
        orderRepository.delete(byId.get());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    public ResponseEntity<Orders> updateOrder(Orders reqOrder, Long id) {
        Optional<Orders> byId = orderRepository.findById(id);
        if(!byId.isPresent()){
            return new ResponseEntity<Orders>((Orders) null,HttpStatus.BAD_REQUEST);
        }
        Orders orders = byId.get();
        if(reqOrder.getCustomerId()!= null)
            orders.setCustomerId(reqOrder.getCustomerId());
        if(reqOrder.getOrderDate()!=null)
            orders.setOrderDate(reqOrder.getOrderDate());
        if(reqOrder.getDiscount()!=null)
            orders.setDiscount(reqOrder.getDiscount());
        if(reqOrder.getTotalPrice()!=null)
            orders.setTotalPrice(reqOrder.getTotalPrice());
        List<OrderProductMapper> reqOrderProductMapperList = reqOrder.getOrderProductMapperList();
        if(!reqOrderProductMapperList.isEmpty()) {
            List<OrderProductMapper> orderProductMapperList = orders.getOrderProductMapperList();
            for(OrderProductMapper reqOrderProductMapper : reqOrderProductMapperList){
                orderProductMapperService.updateOrderProductMapper(reqOrderProductMapper
                    ,reqOrderProductMapper.getOrderProductId());
            }
            orders.setOrderProductMapperList(reqOrderProductMapperList);
        }
        //update the orderProductMapperList's element in orderProductMapper table
        for(OrderProductMapper orderProductMapper : orders.getOrderProductMapperList()){
            orderProductMapperRespository.save(orderProductMapper);
        }
        orderRepository.save(orders);
        return new ResponseEntity<Orders>(orders,HttpStatus.ACCEPTED);
    }

    public ResponseEntity<Orders> getOrderById(Long orderId) {
        Optional<Orders> byId = orderRepository.findById(orderId);
        if(!byId.isPresent())
            return new ResponseEntity<>(byId.get(),HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(byId.get(),HttpStatus.OK);
    }
}
