package com.garvin.bookstore.service;

import com.garvin.bookstore.controller.PurchaseController;
import com.garvin.bookstore.model.PurchaseItemModel;
import com.garvin.bookstore.model.PurchaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    public String getPrice(PurchaseModel purchaseModel) {
        logger.info(purchaseModel.getUserId());
        for (PurchaseItemModel item: purchaseModel.getPurchaseItems()) {
            logger.info(item.getType());
        }
        return "getPrice was called";
    }
}
