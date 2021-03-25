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
import java.util.ArrayList;
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
        Long customerId = order.getCustomerId();
        Long counts = orderRepository.countByCustomerId(customerId);
        //Gets customer through the customerId in order table
        Optional<Customer> byId = customerRepository.findById(customerId);
        //customer id check as if customer doesn't exist in  customer table
        if(!byId.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Customer customer = byId.get();
        //sets its type if condition is meet
        if(counts==9){
            System.out.println("you have placed 9 orders with us"
                + " buy one more stuff with us and get a discount of 10% and get prometed to gold customer");
        }
        if(counts==19){
            System.out.println("you have placed 19 orders with us"
                + " buy one more stuff with us and get a discount of 20% and get prometed to platinum customer");
        }
        if(counts >= 9) {
            customer.setCustomerType(CustomerType.GOLD);
        }
        if(counts >= 19){
            customer.setCustomerType(CustomerType.PLATINUM);
        }
        customerRepository.save(customer);
        updateDiscountAndTotalPrice(customer,order);
        List<OrderProductMapper> addOrderProductMapperList = order.getOrderProductMapperList();
        order.setProductCount((long) order.getOrderProductMapperList().size());
        for(OrderProductMapper orderProductMapper: addOrderProductMapperList){
            orderProductMapper.setOrders(order);
//            orderProductMapperRespository.save(orderProductMapper);
        }

        Orders save = orderRepository.save(order);
        return new ResponseEntity<Orders>(save, HttpStatus.CREATED);

    }

    private void updateDiscountAndTotalPrice(Customer customer, Orders order) {
        Long totalPrice = 0l;
        if(customer.getCustomerType().equals(CustomerType.REGULAR)){
             order.setDiscount(0l);
            for(OrderProductMapper orderProductMapper:order.getOrderProductMapperList()){
                totalPrice += orderProductMapper.getProductQuantity()*orderProductMapper.getProductPrice();
            }
            order.setTotalPrice(totalPrice);
        }
        if(customer.getCustomerType().equals(CustomerType.GOLD)){

            for(OrderProductMapper orderProductMapper:order.getOrderProductMapperList()){
                totalPrice += orderProductMapper.getProductQuantity()*orderProductMapper.getProductPrice();
            }
            order.setTotalPrice(totalPrice);
            //Long totalValue = order.getProductPrice()*order.getProductQuantity();
            Long discount = (totalPrice*10)/100;
            order.setDiscount(discount);
        }
        if(customer.getCustomerType().equals(CustomerType.PLATINUM)){

            for(OrderProductMapper orderProductMapper:order.getOrderProductMapperList()){
                totalPrice += orderProductMapper.getProductQuantity()*orderProductMapper.getProductPrice();
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
            return new ResponseEntity<Orders>(HttpStatus.BAD_REQUEST);

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

        //counts is for check on total no of orders per customers
        //if its <=9 then we have change the customertype and add discount of 10
        Long customerId = reqOrder.getCustomerId();
        Long counts = orderRepository.countByCustomerId(customerId);
        //Gets customer through the customerId in order table
        Optional<Customer> CustomerbyId = customerRepository.findById(customerId);
        //customer id check as if customer doesn't exist in  customer table
        if(!byId.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Customer customer =CustomerbyId.get();
        //sets its type if condition is meet
        if(counts >= 9) {
            customer.setCustomerType(CustomerType.GOLD);
        }
        if(counts >= 19){
            customer.setCustomerType(CustomerType.PLATINUM);
        }
        customerRepository.save(customer);


        Long totalPrice = 0l;
        if(customer.getCustomerType().equals(CustomerType.REGULAR)){
            orders.setDiscount(0l);
            for(OrderProductMapper orderProductMapper:reqOrder.getOrderProductMapperList()){
                totalPrice += orderProductMapper.getProductQuantity()*orderProductMapper.getProductPrice();
            }
            orders.setTotalPrice(totalPrice);
        }
        if(customer.getCustomerType().equals(CustomerType.GOLD)){

            for(OrderProductMapper orderProductMapper:reqOrder.getOrderProductMapperList()){
                totalPrice += orderProductMapper.getProductQuantity()*orderProductMapper.getProductPrice();
            }
            orders.setTotalPrice(totalPrice);
            //Long totalValue = order.getProductPrice()*order.getProductQuantity();
            Long discount = (totalPrice*10)/100;
            orders.setDiscount(discount);
        }
        if(customer.getCustomerType().equals(CustomerType.PLATINUM)){

            for(OrderProductMapper orderProductMapper:reqOrder.getOrderProductMapperList()){
                totalPrice += orderProductMapper.getProductQuantity()*orderProductMapper.getProductPrice();
            }
            orders.setTotalPrice(totalPrice);
            Long discount = (totalPrice*20)/100;
            orders.setDiscount(discount);
        }


        List<OrderProductMapper> reqOrderProductMapperList = reqOrder.getOrderProductMapperList();
        orders.setProductCount((long) reqOrder.getOrderProductMapperList().size());
        if(!reqOrderProductMapperList.isEmpty()) {

            for(OrderProductMapper reqOrderProductMapper : reqOrderProductMapperList){
                reqOrderProductMapper.setOrders(reqOrder);

            }

            //orderProductMapperRespository.deleteByOrderIdAndOrderProductId(orderId,);
            /*
            delete from order-product-mapper where order_id =:? and order_product_id not in();
            Select order_product_id from order_product_mapper

             */
            orders.setOrderProductMapperList(reqOrderProductMapperList);
        }

        orderRepository.save(orders);
        //testing for delete of those order_product_id which are not include in the put request of order
        //Long orderIdForDelete = orders.getOrderId();
        //OrderProductMapper orderProductMapper = orders.getOrderProductMapperList().get(1);
        //orderProductMapperRespository.deleteByOrderIdAndListOfOrderProductId(orderIdForDelete,orderProductMapper);
        return new ResponseEntity<Orders>(HttpStatus.OK);
    }

    public ResponseEntity<Orders> getOrderById(Long orderId) {
        Optional<Orders> byId = orderRepository.findById(orderId);
        if(!byId.isPresent())
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(byId.get(),HttpStatus.OK);
    }
}
