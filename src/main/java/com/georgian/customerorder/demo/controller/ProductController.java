package com.georgian.customerorder.demo.controller;

import com.georgian.customerorder.demo.entity.Product;
import com.georgian.customerorder.demo.service.CustomerService;
import com.georgian.customerorder.demo.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products")
public class ProductController {

  @Autowired
  private CustomerService customerService;
  private ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody Product product){
    ResponseEntity<Product> productResponseEntity = productService.addNewProduct(product);
    return productResponseEntity;
  }

  @PutMapping
  public ResponseEntity<Product> updateProduct(@RequestBody Product product, @RequestParam(value = "id")Long id){
    ResponseEntity<Product> productResponseEntity = productService.updateProduct(product, id);
    return productResponseEntity;
  }

  @GetMapping
  public ResponseEntity<List<Product>> retierevAllProducts()
  {        return productService.retierveAllProducts(); }

  @GetMapping("/id/{id}")
  public ResponseEntity<Product> retierveProductById(@PathVariable(value = "id") Long productId){
    return productService.retierveProductById(productId);
  }

  @DeleteMapping("/id/{id}")
  public ResponseEntity<HttpStatus> deleteProductById(@PathVariable("id") Long productId){
    //return authorRepository.findById(id  );
    ResponseEntity<HttpStatus> productResponseEntity = productService.deleteById(productId);
    return productResponseEntity;
  }

}
