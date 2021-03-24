package com.georgian.customerorder.demo.controller;

import com.georgian.customerorder.demo.entity.Orders;
import com.georgian.customerorder.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping
    public ResponseEntity<Orders> placeOrder(@RequestBody Orders order){
        ResponseEntity<Orders> ordersResponseEntity = (ResponseEntity<Orders>) orderService.placeOrder(order);
        return ordersResponseEntity;
    }


    @PutMapping
    public ResponseEntity<Orders> updateOrder( @RequestBody Orders order,@RequestParam(value = "id") Long id){
        ResponseEntity<Orders> ordersResponseEntity = orderService.updateOrder(order, id);
        return ordersResponseEntity;
    }
    @GetMapping
    public ResponseEntity<List<Orders>> findAllOrder(){

        ResponseEntity<List<Orders>> allOrder = orderService.getAllOrder();

        return allOrder;
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable(value = "id") Long orderId){
        return orderService.getOrderById(orderId);
    }

    @DeleteMapping(path = "/id/{id}")
    public ResponseEntity<Orders> deleteOrderById(@PathVariable(value = "id") Long orderId){
        ResponseEntity<Orders> ordersResponseEntity = orderService.deleteOrderById(orderId);
        return ordersResponseEntity;
    }
}
