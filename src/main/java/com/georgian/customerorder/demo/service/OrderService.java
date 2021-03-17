package com.georgian.customerorder.demo.service;

import com.georgian.customerorder.demo.entity.Customer;
import com.georgian.customerorder.demo.entity.CustomerType;
import com.georgian.customerorder.demo.entity.Orders;
import com.georgian.customerorder.demo.repository.CustomerRepository;
import com.georgian.customerorder.demo.repository.OrderRepository;
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

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public ResponseEntity<Orders> placeOrder(Orders order) {
        Long counts = orderRepository.countByCustomerId(order.getCustomerId());
        Optional<Customer> byId = customerRepository.findById(order.getCustomerId());
        Customer customer =byId.get();

        if(counts >= 9) {
            customer.setCustomerType(CustomerType.GOLD);
        }
        if(counts >= 19){
            customer.setCustomerType(CustomerType.PLATINUM);
        }
        customerRepository.save(customer);
        updateDiscount(customer,order);
        Orders save = orderRepository.save(order);
        return new ResponseEntity<Orders>(save, HttpStatus.CREATED);

    }

    private void updateDiscount(Customer customer, Orders order) {

        if(customer.getCustomerType().equals(CustomerType.REGULAR)){
             order.setDiscount(0l);
        }
        if(customer.getCustomerType().equals(CustomerType.GOLD)){
            Long totalValue = order.getProductPrice()*order.getProductQuantity();
            Long discount = (totalValue*10)/100;
            order.setDiscount(discount);
        }
        if(customer.getCustomerType().equals(CustomerType.PLATINUM)){
            Long totalValue = order.getProductPrice()*order.getProductQuantity();
            Long discount = (totalValue*20)/100;
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
        if(reqOrder.getProductName()!=null)
            orders.setProductName(reqOrder.getProductName());
        if(reqOrder.getProductQuantity()!=null)
            orders.setProductQuantity(reqOrder.getProductQuantity());
        if(reqOrder.getProductPrice()!=null)
            orders.setProductPrice(reqOrder.getProductPrice());
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
