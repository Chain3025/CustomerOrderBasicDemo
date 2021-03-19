package com.georgian.customerorder.demo.service;

import com.georgian.customerorder.demo.entity.Product;
import com.georgian.customerorder.demo.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  @Autowired
  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public ResponseEntity<Product> addNewProduct(Product product) {
    Product save = productRepository.save(product);
    return new ResponseEntity<>(save, HttpStatus.ACCEPTED);
  }

  public ResponseEntity<Product> updateProduct(Product reqProduct, Long id) {
    Optional<Product> byId = productRepository.findById(id);
    if(!byId.isPresent()){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    Product product =byId.get();
    product.setProductPrice(reqProduct.getProductPrice());
    product.setProductName(reqProduct.getProductName());
    Product updatedProduct = productRepository.save(product);
    return new ResponseEntity<>(updatedProduct,HttpStatus.ACCEPTED);
  }

  public ResponseEntity<Product> retierveProductById(Long productId) {
    Optional<Product> byId = productRepository.findById(productId);
    if(!byId.isPresent()){
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<Product>(byId.get(),HttpStatus.OK);
  }

  public ResponseEntity<List<Product>> retierveAllProducts() {
    List<Product> all = productRepository.findAll();
    if(all.size()<=0)
      return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
    return new ResponseEntity<>(all,HttpStatus.OK);
  }

  public ResponseEntity<HttpStatus> deleteById(Long productId) {
    Optional<Product> byId = productRepository.findById(productId);
    if(!byId.isPresent())
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    productRepository.delete(byId.get());
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }
}
