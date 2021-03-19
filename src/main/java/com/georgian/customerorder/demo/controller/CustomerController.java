package com.georgian.customerorder.demo.controller;

import com.georgian.customerorder.demo.entity.Customer;
import com.georgian.customerorder.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CustomerController {


    @Autowired
    private CustomerService customerService;


    @PostMapping("/customers")
    public ResponseEntity<Customer> createAuthor(@RequestBody Customer customer){
        ResponseEntity<Customer> customerResponseEntity = customerService.addNewCustomer(customer);
        return customerResponseEntity;
    }

    @PutMapping("/customers")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @RequestParam(value = "id")Long id){
        ResponseEntity<Customer> customerResponseEntity = customerService.updateCustomer(customer, id);
        return customerResponseEntity;
    }

    @GetMapping("/customers")
    public List<Customer> retierevAllCustomer()
    {        return customerService.retierveAllCustomer(); }

    @GetMapping("/customers/id/{id}")
    public Customer retierveCustomerById(@PathVariable(value = "id") Long customerId){
        return customerService.retierveCustomerById(customerId);
    }



    @DeleteMapping("/customers/id/{id}")
    public ResponseEntity<Customer> deleteCustomerById(@PathVariable("id") Long customerId){
        //return authorRepository.findById(id  );
        ResponseEntity<Customer> customerResponseEntity = customerService.deleteById(customerId);
        return customerResponseEntity;

    }

}
