# Introduction

A backend for a simple bookstore.

## Running this application

Assuming Java 17 is installed, running the following sequence of commands will start this application: 
```
mvn clean install
cd target
java -jar bookstore-0.0.1-SNAPSHOT.jar
```

If you see an error along the lines of:
```
[ERROR] /path/to/src/main/java/com/garvin/bookstore/BookstoreApplication.java:[3,32] cannot access org.springframework.boot.SpringApplication
  bad class file: /path/to/repository/org/springframework/boot/spring-boot/3.4.3/spring-boot-3.4.3.jar(org/springframework/boot/S
pringApplication.class)
    class file has wrong version 61.0, should be 52.0
    Please remove or make sure it appears in the correct subdirectory of the classpath.
```
then maven is using a java version 8. On Ubuntu, folow the instructions in the section "Install OpenJDK 17 from Ubuntu 20.04 Repository" in [this document](https://www.rosehosting.com/blog/how-to-install-java-17-lts-on-ubuntu-20-04/).
Now run:
```
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```
The sequence of commands above will now start the application.

## Usage

The API has the following endpoints:

GET `/books/{isbn}` - get details of an individual book
GET `/books` - get details of all books
GET `/customers/{userId}` - get the loyalty points for an individual customer
GET `/customers` - get the loyalty points for all customers
GET `/purchase` - confirm what the outcome of a purchase will be
POST `/purchase` - perform a purchase

Example requests and notes on the request/response bodies are in the following sections.

### Returning the books available for purchase
`base_price` is the price of the book before any modifiers for type or bundle size are applied.
There can be up to 3 entries in the `inventory` array, one for each type. If a type is not present in the array then it is not currently available.

#### Individual book
GET `/books/{isbn}`:
```
$ curl -X GET http://localhost:8080/books/9781447273301
{
  "title": "Children of Time",
  "author": "Adrian Tchaikovsky",
  "isbn": 9781447273301,
  "base_price": 10.00,
  "inventory": [
    {
      "type": "N",
      "stock": 7
    }
  ]
}
```

#### All books
GET `/books`
```
$ curl -X GET http://localhost:8080/books
[
  {
    "title": "The Iliad",
    "author": "Emily Wilson",
    "isbn": 9781324001805,
    "base_price": 20,
    "inventory": [
      {
        "type": "O",
        "stock": 5
      }
    ]
  },
  {
    "title": "Children of Time",
    "author": "Adrian Tchaikovsky",
    "isbn": 9781447273301,
    "base_price": 10,
    "inventory": [
      {
        "type": "N",
        "stock": 7
      }
    ]
  },
  ...
]
```

### Returning the loyalty points for a customer

#### Individual customer

GET `/customers/{userId}`:
```
$ curl -X GET http://localhost:8080/customers/0E4NJH584C
{"userId":"0E4NJH584C","loyaltyPoints":23}
```

#### All customers

GET `/customers`:
```
[
  {
    "userId": "S0PPQX6O1U",
    "loyaltyPoints": 12
  },
  {
    "userId": "0E4NJH584C",
    "loyaltyPoints": 23
  },
  ...
]
```

### Purchase
The GET request is offered to allow a user to confirm what the outcome of a purchase will be before attempting the purchase.

The `purchaseItems` array indicates books that the customer wishes to pay for. The `freeItems` indicates books which the customer wishes to claim using loyalty points. Both arrays have the same format.

Note that both arrays may contain multiple entries for the same `isbn`/`type` pair. The `quantity` will be summed in this case.

The response body indicates the cost of the `purchaseItems` in `totalCost` and the change due to the loyalty points in `loyaltyPointsAdjustment`.

The purchase may not be possible due to insufficient stock or loyalty points. `canComplete` is `true` and `status` is `OK` when the purchase is possible. Otherwise  `canComplete` is `false` and `status` contains short message clarifying why this is the case. 

#### Confirm that a purchase can complete
GET /purchase
```
$ curl -H "Content-Type: application/json" -X GET --data '{"userId": "EORIZQA123", "purchaseItems": [{"isbn": "9781324001805", "type": "N", "quantity": "2"}], "freeItems": []}' http://localhost:8080/purchase
{
  "userId": "EORIZQA123",
  "totalCost": 40.00,
  "loyaltyPointsAdjustment": 2,
  "canComplete": true,
  "status": "OK"
}
```

#### Make a purchase
POST /purchase
```
$ curl -H "Content-Type: application/json" -X POST --data '{"userId": "EORIZQA123", "purchaseItems": [{"isbn": "9781324001805", "type": "N", "quantity": "2"}], "freeItems": []}' http://localhost:8080/purchase
Output is the same format as the GET request.
```

## Design Decisions

- Spring popular framework, 
- MVC, well understood paradigm 
- H2, handy for prototyping
- discuss db
-- not using ISBN as key
-- distinct table for books v inv, normalisation, prices can be determined from base price of book
-- composite key on inventory
-- user_id field in customer table, security, protect db details from attacker
- types in config file for convenience, also added other stuff to avoid magic numbers
- assumptions re: loyalty points
-- 'pay' 10 per free book
-- do not consider free books as part of bundle
- discuss purchaseTracker
-- pulls purchase inventory out of the service
-- consideration of better key (use tuple instead)
-- acknowledge looping through list, possible inefficiency
- float possibility of a dedicated loyalty point class
- discuss tests
- disscuss return server internal error
