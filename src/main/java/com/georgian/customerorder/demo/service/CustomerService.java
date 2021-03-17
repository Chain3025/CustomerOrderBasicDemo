package com.georgian.customerorder.demo.service;

import com.georgian.customerorder.demo.entity.Customer;
import com.georgian.customerorder.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

   // Logger log = (Logger) LoggerFactory.getLogger(CustomerService.class);
    @Autowired
    private CustomerRepository customerRepository;


    public ResponseEntity<Customer> addNewCustomer(Customer customer) {
        Customer save = customerRepository.save(customer);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }
    public ResponseEntity<Customer> updateCustomer(Customer reqCustomer, Long id) {

        Optional<Customer> byId = customerRepository.findById(id);
        if(!byId.isPresent()){
            ResponseEntity<Customer> customerResponseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            return customerResponseEntity;
        }
        Customer customer = byId.get();
        if(reqCustomer.getCustomerName() !=null)
            customer.setCustomerName(reqCustomer.getCustomerName());
        if(reqCustomer.getCustomerEmail()!=null)
            customer.setCustomerEmail(reqCustomer.getCustomerEmail());
        if(reqCustomer.getCustomerType()!=null)
            customer.setCustomerType(reqCustomer.getCustomerType());

        Customer save = customerRepository.save(customer);
        return new ResponseEntity<Customer>(save,HttpStatus.ACCEPTED);
    }

    public List<Customer> retierveAllCustomer() {
        List<Customer> all = customerRepository.findAll();
        return all;
    }

    public Customer retierveCustomerById(Long id) {
        Optional<Customer> byId = customerRepository.findById(id);

        return byId.get();
    }


    public ResponseEntity<Customer> deleteById(Long id) {
        Optional<Customer> byId = customerRepository.findById(id);
        if(!byId.isPresent()){
            ResponseEntity<Customer> customerResponseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return customerResponseEntity;
        }

        customerRepository.delete(byId.get());
        //return allById;
        return new ResponseEntity<>(null,HttpStatus.ACCEPTED);
    }


}
