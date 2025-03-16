This .json files in this directory have been used as the body for `purchase` requests when testing the application. They can also be sused as examples for constructing your own requests.
Here is an example of thier use:
```
curl -H "Content-Type: application/json" -X GET --data "@manual_tests/purchase.json" http://localhost:8080/purchase
```

The following scnearios covered:
`claimNew.json` - claim, using loyalty points, a new book 
`claimNotEnoughStock.json` - claim a book that does not have sufficient stock
`nonExistentCustomer.json` - a request for a non-exisitent customer
`purchase.json` - purchase a single book
`purchase2.json` - purchase two books
`purchase2Claim1.json` - purchase two books and claim a free book
`purchase3.json` - purchase three books
`purchase3Claim1.json` - purchase three books and claim a free book
`purchaseNotEnoughStock.json` - purchase a book that does not have sufficient stock
`purchaseNotInDb.json` - purchase a book that is not in the books table 
`purchaseNotInInv.json` - purchase a book that is in the books table but not in the inventory
`repeatClaim.json` - claim two books using two entries in the `freeItems` array
`repeatPurchase.json` - claim two books using two entries in the `purchaseItems` array
