package com.garvin.bookstore.controller;

import com.garvin.bookstore.model.CustomerModel;
import com.garvin.bookstore.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    CustomerService customerService;

    @GetMapping("/{userId}")
    public CustomerModel getCustomer(@PathVariable String userId) {
        logger.info("GET /customers/{userId} endpoint called");
        return customerService.getCustomer(userId);
    }

    @GetMapping()
    public List<CustomerModel> getCustomers() {
        logger.info("GET /customers endpoint called");
        return customerService.getCustomers();
    }
}