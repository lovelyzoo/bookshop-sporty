package com.garvin.bookstore.controller;

import com.garvin.bookstore.model.CustomerModel;
import com.garvin.bookstore.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/{userId}")
    public CustomerModel getBook(@PathVariable String userId) {
        return customerService.getCustomer(userId);
    }

    @GetMapping()
    public List<CustomerModel> getBooks() {
        return customerService.getCustomers();
    }
}
