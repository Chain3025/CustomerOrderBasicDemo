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
<<<<<<< HEAD
import java.util.ArrayList;
import org.hibernate.criterion.Order;
=======
>>>>>>> feature-inventory
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

    public Object placeOrder(Orders order) {
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
<<<<<<< HEAD
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
=======
>>>>>>> feature-inventory
        Long totalPrice = 0l;
        Long quantity = 0l;
        if(customer.getCustomerType().equals(CustomerType.REGULAR)){
            order.setDiscount(0l);
            for(OrderProductMapper orderProductMapper:order.getOrderProductMapperList()){
                Long productId = orderProductMapper.getProductId();
                Optional<Product> byIdProduct = productRepository.findById(productId);
                if(!byIdProduct.isPresent()){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                Product product =byIdProduct.get();
                Long totalProductQuantity = product.getTotalProductQuantity();
                quantity =orderProductMapper.getProductQuantity();
                if(totalProductQuantity<quantity){
                    System.out.println("requested product quanity is not in stock");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                Long residueProductQuantity = totalProductQuantity - quantity;
                product.setTotalProductQuantity(residueProductQuantity);
                productRepository.save(product);
                totalPrice += quantity*orderProductMapper.getProductPrice();

            }
            order.setTotalPrice(totalPrice);
        }
        if(customer.getCustomerType().equals(CustomerType.GOLD)){

            for(OrderProductMapper orderProductMapper:order.getOrderProductMapperList()){
                Long productId = orderProductMapper.getProductId();
                Optional<Product> byIdProduct = productRepository.findById(productId);
                if(!byIdProduct.isPresent()){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                Product product =byIdProduct.get();
                Long totalProductQuantity = product.getTotalProductQuantity();
                quantity =orderProductMapper.getProductQuantity();
                if(totalProductQuantity<quantity){
                    System.out.println("requested product quanity is not in stock");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                Long residueProductQuantity = totalProductQuantity - quantity;
                product.setTotalProductQuantity(residueProductQuantity);
                productRepository.save(product);
                totalPrice += quantity*orderProductMapper.getProductPrice();
            }
            Long discount = (totalPrice*10)/100;
            order.setTotalPrice(totalPrice-discount);
            //Long totalValue = order.getProductPrice()*order.getProductQuantity();

            order.setDiscount(discount);
        }
        if(customer.getCustomerType().equals(CustomerType.PLATINUM)){

            for(OrderProductMapper orderProductMapper:order.getOrderProductMapperList()){

                Long productId = orderProductMapper.getProductId();
                Optional<Product> byIdProduct = productRepository.findById(productId);
                if(!byIdProduct.isPresent()){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                Product product =byIdProduct.get();
                Long totalProductQuantity = product.getTotalProductQuantity();
                quantity =orderProductMapper.getProductQuantity();
                if(totalProductQuantity<quantity){
                    System.out.println("requested product quanity is not in stock");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                Long residueProductQuantity = totalProductQuantity - quantity;
                product.setTotalProductQuantity(residueProductQuantity);
                productRepository.save(product);
                totalPrice += quantity*orderProductMapper.getProductPrice();
            }
            Long discount = (totalPrice*20)/100;
            order.setTotalPrice(totalPrice-discount);
            //Long totalValue = order.getProductPrice()*order.getProductQuantity();

            order.setDiscount(discount);
        }

        List<OrderProductMapper> addOrderProductMapperList = order.getOrderProductMapperList();
        for(OrderProductMapper orderProductMapper: addOrderProductMapperList){
            orderProductMapper.setOrders(order);
//            orderProductMapperRespository.save(orderProductMapper);
        }

        Orders save = orderRepository.save(order);
        return new ResponseEntity<Orders>(save, HttpStatus.CREATED);

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

        Orders orders = byId.get();
        List<OrderProductMapper> orderProductMapperList = orders.getOrderProductMapperList();
        orderProductMapperList.stream().forEach(orderProductMapper -> {
            Long productId = orderProductMapper.getProductId();
            Optional<Product> byIdProduct = productRepository.findById(productId);
            Product product = byIdProduct.get();
            Long updateQuantity =product.getTotalProductQuantity()+orderProductMapper.getProductQuantity();
            product.setTotalProductQuantity(updateQuantity);
            productRepository.save(product);
        });
        orderRepository.delete(orders);
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
<<<<<<< HEAD


        Long totalPrice = 0l;
        if(customer.getCustomerType().equals(CustomerType.REGULAR)){
            orders.setDiscount(0l);
            for(OrderProductMapper orderProductMapper:reqOrder.getOrderProductMapperList()){
                totalPrice += orderProductMapper.getProductQuantity()*orderProductMapper.getProductPrice();
=======
        Long totalPrice = 0l;

        Long reqQuantity=0l;
        Long alreadyQuantity=0l;
        Long updatedQuantity=0l;
        if(customer.getCustomerType().equals(CustomerType.REGULAR)){
            orders.setDiscount(0l);
            for(OrderProductMapper reqOrderProductMapper:reqOrder.getOrderProductMapperList()){

                Long orderProductId = reqOrderProductMapper.getOrderProductId();
                //this check is for adding new orderProduct which are to be created so their id is not passed
                if(orderProductId!=null){
                    Optional<OrderProductMapper> byIdOrderProductMapper = orderProductMapperRespository
                        .findById(orderProductId);
                    //check for its corresponding product in db;
                    if(byIdOrderProductMapper.isPresent()){

                        OrderProductMapper orderProductMapperFromDB = byIdOrderProductMapper.get();
                        alreadyQuantity = orderProductMapperFromDB.getProductQuantity();

                        Long productId = reqOrderProductMapper.getProductId();
                        Optional<Product> byIdProduct = productRepository.findById(productId);
                        if(!byIdProduct.isPresent()){
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        Product product =byIdProduct.get();
                        Long totalProductQuantity = product.getTotalProductQuantity();
                        reqQuantity =reqOrderProductMapper.getProductQuantity();
                        if(reqQuantity > alreadyQuantity){
                            updatedQuantity = reqQuantity - alreadyQuantity;
                        }else
                            updatedQuantity = alreadyQuantity -reqQuantity;

                        if(totalProductQuantity<reqQuantity){
                            System.out.println("requested product quanity is not in stock");
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        Long residueProductQuantity = totalProductQuantity - updatedQuantity;
                        product.setTotalProductQuantity(residueProductQuantity);
                        productRepository.save(product);
                        totalPrice += reqQuantity*reqOrderProductMapper.getProductPrice();
                    }else{

                        Long productId = reqOrderProductMapper.getProductId();
                        Optional<Product> byIdProduct = productRepository.findById(productId);
                        if(!byIdProduct.isPresent()){
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        Product product =byIdProduct.get();
                        Long totalProductQuantity = product.getTotalProductQuantity();
                        reqQuantity =reqOrderProductMapper.getProductQuantity();
                        if(totalProductQuantity < reqQuantity){
                            System.out.println("requested product quanity is not in stock");
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        Long residueProductQuantity = totalProductQuantity - reqQuantity;
                        product.setTotalProductQuantity(residueProductQuantity);
                        productRepository.save(product);
                        totalPrice += reqQuantity*reqOrderProductMapper.getProductPrice();

                    }
                }else{
                    Long productId = reqOrderProductMapper.getProductId();
                    Optional<Product> byIdProduct = productRepository.findById(productId);
                    if(!byIdProduct.isPresent()){
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    Product product =byIdProduct.get();
                    Long totalProductQuantity = product.getTotalProductQuantity();
                    reqQuantity =reqOrderProductMapper.getProductQuantity();
                    if(totalProductQuantity<reqQuantity){
                        System.out.println("requested product quanity is not in stock");
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    Long residueProductQuantity = totalProductQuantity - reqQuantity;
                    product.setTotalProductQuantity(residueProductQuantity);
                    productRepository.save(product);
                    totalPrice += reqQuantity*reqOrderProductMapper.getProductPrice();

                }

>>>>>>> feature-inventory
            }
            orders.setTotalPrice(totalPrice);
        }
        if(customer.getCustomerType().equals(CustomerType.GOLD)){

<<<<<<< HEAD
            for(OrderProductMapper orderProductMapper:reqOrder.getOrderProductMapperList()){
                totalPrice += orderProductMapper.getProductQuantity()*orderProductMapper.getProductPrice();
            }
            orders.setTotalPrice(totalPrice);
            //Long totalValue = order.getProductPrice()*order.getProductQuantity();
            Long discount = (totalPrice*10)/100;
=======
            for(OrderProductMapper reqOrderProductMapper:reqOrder.getOrderProductMapperList()){

                Long orderProductId = reqOrderProductMapper.getOrderProductId();
                //this check is for adding new orderProduct which are to be created so their id is not passed
                if(orderProductId!=null){
                    Optional<OrderProductMapper> byIdOrderProductMapper = orderProductMapperRespository
                        .findById(orderProductId);
                    //check for its corresponding product in db;
                    if(byIdOrderProductMapper.isPresent()){

                        OrderProductMapper orderProductMapperFromDB = byIdOrderProductMapper.get();
                        alreadyQuantity = orderProductMapperFromDB.getProductQuantity();

                        Long productId = reqOrderProductMapper.getProductId();
                        Optional<Product> byIdProduct = productRepository.findById(productId);
                        if(!byIdProduct.isPresent()){
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        Product product =byIdProduct.get();
                        Long totalProductQuantity = product.getTotalProductQuantity();
                        reqQuantity =reqOrderProductMapper.getProductQuantity();
                        if(reqQuantity > alreadyQuantity){
                            updatedQuantity = reqQuantity - alreadyQuantity;
                        }else
                            updatedQuantity = alreadyQuantity -reqQuantity;

                        if(totalProductQuantity<reqQuantity){
                            System.out.println("requested product quanity is not in stock");
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        Long residueProductQuantity = totalProductQuantity - updatedQuantity;
                        product.setTotalProductQuantity(residueProductQuantity);
                        productRepository.save(product);
                        totalPrice += reqQuantity*reqOrderProductMapper.getProductPrice();
                    }else{

                        Long productId = reqOrderProductMapper.getProductId();
                        Optional<Product> byIdProduct = productRepository.findById(productId);
                        if(!byIdProduct.isPresent()){
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        Product product =byIdProduct.get();
                        Long totalProductQuantity = product.getTotalProductQuantity();
                        reqQuantity =reqOrderProductMapper.getProductQuantity();
                        if(totalProductQuantity < reqQuantity){
                            System.out.println("requested product quanity is not in stock");
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        Long residueProductQuantity = totalProductQuantity - reqQuantity;
                        product.setTotalProductQuantity(residueProductQuantity);
                        productRepository.save(product);
                        totalPrice += reqQuantity*reqOrderProductMapper.getProductPrice();

                    }
                }else{
                    Long productId = reqOrderProductMapper.getProductId();
                    Optional<Product> byIdProduct = productRepository.findById(productId);
                    if(!byIdProduct.isPresent()){
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    Product product =byIdProduct.get();
                    Long totalProductQuantity = product.getTotalProductQuantity();
                    reqQuantity =reqOrderProductMapper.getProductQuantity();
                    if(totalProductQuantity<reqQuantity){
                        System.out.println("requested product quanity is not in stock");
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    Long residueProductQuantity = totalProductQuantity - reqQuantity;
                    product.setTotalProductQuantity(residueProductQuantity);
                    productRepository.save(product);
                    totalPrice += reqQuantity*reqOrderProductMapper.getProductPrice();

                }

            }
            Long discount = (totalPrice*10)/100;
            orders.setTotalPrice(totalPrice-discount);
            //Long totalValue = order.getProductPrice()*order.getProductQuantity();

>>>>>>> feature-inventory
            orders.setDiscount(discount);
        }
        if(customer.getCustomerType().equals(CustomerType.PLATINUM)){

<<<<<<< HEAD
            for(OrderProductMapper orderProductMapper:reqOrder.getOrderProductMapperList()){
                totalPrice += orderProductMapper.getProductQuantity()*orderProductMapper.getProductPrice();
            }
            orders.setTotalPrice(totalPrice);
            Long discount = (totalPrice*20)/100;
            orders.setDiscount(discount);
        }

=======
            for(OrderProductMapper reqOrderProductMapper:reqOrder.getOrderProductMapperList()){

                Long orderProductId = reqOrderProductMapper.getOrderProductId();
                //this check is for adding new orderProduct which are to be created so their id is not passed
                if(orderProductId!=null){
                    Optional<OrderProductMapper> byIdOrderProductMapper = orderProductMapperRespository
                        .findById(orderProductId);
                    //check for its corresponding product in db;
                    if(byIdOrderProductMapper.isPresent()){

                        OrderProductMapper orderProductMapperFromDB = byIdOrderProductMapper.get();
                        alreadyQuantity = orderProductMapperFromDB.getProductQuantity();

                        Long productId = reqOrderProductMapper.getProductId();
                        Optional<Product> byIdProduct = productRepository.findById(productId);
                        if(!byIdProduct.isPresent()){
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        Product product =byIdProduct.get();
                        Long totalProductQuantity = product.getTotalProductQuantity();
                        reqQuantity =reqOrderProductMapper.getProductQuantity();
                        if(reqQuantity > alreadyQuantity){
                            updatedQuantity = reqQuantity - alreadyQuantity;
                        }else
                            updatedQuantity = alreadyQuantity -reqQuantity;

                        if(totalProductQuantity<reqQuantity){
                            System.out.println("requested product quanity is not in stock");
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        Long residueProductQuantity = totalProductQuantity - updatedQuantity;
                        product.setTotalProductQuantity(residueProductQuantity);
                        productRepository.save(product);
                        totalPrice += reqQuantity*reqOrderProductMapper.getProductPrice();
                    }else{

                        Long productId = reqOrderProductMapper.getProductId();
                        Optional<Product> byIdProduct = productRepository.findById(productId);
                        if(!byIdProduct.isPresent()){
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        Product product =byIdProduct.get();
                        Long totalProductQuantity = product.getTotalProductQuantity();
                        reqQuantity =reqOrderProductMapper.getProductQuantity();
                        if(totalProductQuantity < reqQuantity){
                            System.out.println("requested product quanity is not in stock");
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        Long residueProductQuantity = totalProductQuantity - reqQuantity;
                        product.setTotalProductQuantity(residueProductQuantity);
                        productRepository.save(product);
                        totalPrice += reqQuantity*reqOrderProductMapper.getProductPrice();

                    }
                }else{
                    Long productId = reqOrderProductMapper.getProductId();
                    Optional<Product> byIdProduct = productRepository.findById(productId);
                    if(!byIdProduct.isPresent()){
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    Product product =byIdProduct.get();
                    Long totalProductQuantity = product.getTotalProductQuantity();
                    reqQuantity =reqOrderProductMapper.getProductQuantity();
                    if(totalProductQuantity<reqQuantity){
                        System.out.println("requested product quanity is not in stock");
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    Long residueProductQuantity = totalProductQuantity - reqQuantity;
                    product.setTotalProductQuantity(residueProductQuantity);
                    productRepository.save(product);
                    totalPrice += reqQuantity*reqOrderProductMapper.getProductPrice();

                }

            }
            Long discount = (totalPrice*20)/100;
            orders.setTotalPrice(totalPrice-discount);
            orders.setDiscount(discount);
        }
>>>>>>> feature-inventory

        List<OrderProductMapper> reqOrderProductMapperList = reqOrder.getOrderProductMapperList();
        orders.setProductCount((long) reqOrder.getOrderProductMapperList().size());
        if(!reqOrderProductMapperList.isEmpty()) {

            for(OrderProductMapper reqOrderProductMapper : reqOrderProductMapperList){
                reqOrderProductMapper.setOrders(reqOrder);

            }

<<<<<<< HEAD
            //orderProductMapperRespository.deleteByOrderIdAndOrderProductId(orderId,);
            /*
            delete from order-product-mapper where order_id =:? and order_product_id not in();
            Select order_product_id from order_product_mapper

             */
=======

>>>>>>> feature-inventory
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
