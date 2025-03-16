package com.garvin.bookstore.service;

import com.garvin.bookstore.db.CustomerEntity;
import com.garvin.bookstore.db.CustomerRepository;
import com.garvin.bookstore.model.CustomerModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public CustomerModel getCustomer(String userId) {
        CustomerModel returnValue = new CustomerModel();

        CustomerEntity customerEntity = customerRepository.findByUserId(userId);
        BeanUtils.copyProperties(customerEntity, returnValue);
        return returnValue;
    }

    public List<CustomerModel> getCustomers() {
        List<CustomerModel> returnValue = new ArrayList<>();

        List<CustomerEntity> customerEntities = customerRepository.findAll();
        for (CustomerEntity customerEntity: customerEntities) {
            CustomerModel customerModel = new CustomerModel();
            BeanUtils.copyProperties(customerEntity, customerModel);
            returnValue.add(customerModel);
        }

        return returnValue;
    }
}
