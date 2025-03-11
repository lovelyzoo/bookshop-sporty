package com.garvin.bookstore.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("inventory")
public class InventoryController {
    @PutMapping
    public String incrementInventory() {
        return "incrementInventory was called\n";
    }

}
