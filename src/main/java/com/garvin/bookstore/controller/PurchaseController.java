package com.garvin.bookstore.controller;

import com.garvin.bookstore.model.PurchaseModel;
import com.garvin.bookstore.model.PurchaseModelResp;
import com.garvin.bookstore.service.PurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("purchase")
public class PurchaseController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    PurchaseService purchaseService;

    @GetMapping
    public PurchaseModelResp getOutcome(@RequestBody PurchaseModel purchaseModel) {
        logger.info("GET /purchase endpoint called");

        return purchaseService.getOutcome(purchaseModel);
    }

    @PostMapping
    public PurchaseModelResp makePurchase(@RequestBody PurchaseModel purchaseModel) {
        logger.info("POST /purchase endpoint called");

        return purchaseService.makePurchase(purchaseModel);
    }
}