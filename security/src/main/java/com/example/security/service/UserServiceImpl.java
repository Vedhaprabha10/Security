package com.example.security.service;

import com.example.security.DTO.Product;
import com.example.security.Entity.UserInfo;
import com.example.security.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserServiceImpl {

    List<Product> products = null;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void loadProductsFromDB() {
        products = IntStream.rangeClosed(1, 100).mapToObj(i -> Product.builder()
                .productId(i).name("product " + i)
                .qty(new Random().nextInt(10))
                .price(new Random().nextInt(5000)).build())
                .collect(Collectors.toList());
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Product getProduct(int id) {
        return products.stream().filter(product -> product.getProductId() == id)
                .findAny()
                .orElseThrow(() -> new RuntimeException("product " + id + " not found"));
    }

    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userRepository.save(userInfo);
        return "user add successfully";
    }
}
