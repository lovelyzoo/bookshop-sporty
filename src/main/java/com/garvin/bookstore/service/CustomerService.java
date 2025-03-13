package com.garvin.bookstore.service;

import com.garvin.bookstore.entity.CustomerEntity;
import com.garvin.bookstore.entity.CustomerRepository;
import com.garvin.bookstore.model.CustomerModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    CustomerRepository customerRepository;

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
