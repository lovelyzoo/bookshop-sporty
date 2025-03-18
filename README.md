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

GET `/books/{isbn}` - get details and inventory of an individual book
GET `/books` - get details and inventory of all books
POST `/books` - add a book
PUT `/books` - update book details
DELETE `/books/{isbn}` - remove a book
POST `/inventory/{isbn}` - add inventory to a book
PUT `/inventory/{isbn}` - update book inventory
DELETE `/inventory/{isbn}` - remove inventory from a book
GET `/customers/{userId}` - get loyalty points for an individual customer
GET `/customers` - get loyalty points for all customers
GET `/purchase` - determine what the outcome of a purchase will be
POST `/purchase` - make a purchase

Example requests and notes on the request/response bodies are in the following sections.

### Get details and inventory of books
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

### Add a book
POST `/books`
```
$ curl -H "Content-Type: application/json" -X POST --data '{"title":"Get in the Van","author":"Henry Rollins","isbn":9781880985243,"basePrice":17.00}' http://localhost:8080/books
{"operationResult":"SUCCESS","operationName":"ADD_BOOK"}
```

### Update book details
PUT `/books`
```
$ curl -H "Content-Type: application/json" -X PUT --data '{"title":"My diary","author":"Me","isbn":9781880985243,"basePrice":13.00}' http://localhost:8080/books
{"operationResult":"SUCCESS","operationName":"UPDATE_BOOK"}
```

### Remove a book
DELETE `/books/{isbn}`
```
$ curl -X DELETE http://localhost:8080/books/9781880985243
{"operationResult":"SUCCESS","operationName":"DELETE_BOOK"}
```

### Add inventory to a book
POST `/inventory`:
```
$ curl -H "Content-Type: application/json" -X POST --data '{"type": "O", "stock": "3"}' http://localhost:8080/inventory/9780593730249
{"operationResult":"SUCCESS","operationName":"ADD_INVENTORY"}
```

### Update inventory for a book
PUT `/inventory`:
```
$ curl -H "Content-Type: application/json" -X PUT --data '{"type": "O", "stock": "2"}' http://localhost:8080/inventory/9780593730249
```

### Delete inventory for a book
DELETE `/inventory`:
Note that this method should only be called once all inventory elements have been removed from the book.
```
$ curl -H "Content-Type: application/json" -X DELETE --data '{"type": "O"}' http://localhost:8080/inventory/9780593730249
{"operationResult":"SUCCESS","operationName":"DELETE_INVENTORY"}
```

### Get customer loyalty points

#### Individual customer

GET `/customers/{userId}`:
```
$ curl -X GET http://localhost:8080/customers/0E4NJH584C
{"userId":"0E4NJH584C","loyaltyPoints":23}
```

#### All customers

GET `/customers`:
```
$ curl -X GET http://localhost:8080/customers
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
The GET method confirms what the outcome of a purchase will be, i.e., it allows the user to check if the purchase is possible and, if so, what the cost and adjustment to loyalty points will be. The POST method makes the purchase and will entail database changes.

Please also see `manual_tests/README.md` for further examples of request bodies.

#### Determine what the outcome of a purchase will be
GET `/purchase`:

Within the request body, the `purchaseItems` array indicates books that the customer wishes to pay for. The `freeItems` indicates books which the customer wishes to claim using loyalty points. Both arrays have the same format.

Note that both arrays may contain multiple entries for the same `isbn`/`type` pair.  When this happens, the `quantity` will be summed over all the entries for the`isbn`/`type` pair.
```
$ curl -H "Content-Type: application/json" -X GET --data '{"userId": "EORIZQA123", "purchaseItems": [{"isbn": "9781324001805", "type": "N", "quantity": "2"}], "freeItems": []}' http://localhost:8080/purchase
```

The response body indicates the cost of the `purchaseItems` in `totalCost` and the change due to the loyalty points in `loyaltyPointsAdjustment`.

The purchase may not be possible due to insufficient stock or loyalty points. `canComplete` is `true` and `status` is `OK` when the purchase is possible. Otherwise  `canComplete` is `false` and `status` contains short message clarifying why this is the case. 
```
{
  "userId": "EORIZQA123",
  "totalCost": 40.00,
  "loyaltyPointsAdjustment": 2,
  "canComplete": true,
  "status": "OK"
}
```

#### Make a purchase
POST `/purchase`:

The POST request is exactly the same as the GET request bar the change to the method. The response is identical as well.
```
$ curl -H "Content-Type: application/json" -X POST --data '{"userId": "EORIZQA123", "purchaseItems": [{"isbn": "9781324001805", "type": "N", "quantity": "2"}], "freeItems": []}' http://localhost:8080/purchase
```

## Database

The application makes use of an H2 database, which has a console at: `http://localhost:8080/h2-console`. The defaults should be fine to connct but here are the relevant values, just in case.
Driver class: `org.h2.Driver` 
JDBC URL: `jdbc:h2:mem:testdb`
User Name: `sa`
Leave the password blank.

## Design Decisions

I've written this application using the Spring boot framework with an H2 database. These framework/database 'just work' out of the box and lend themselves to the rapid prototyping required for an exercise such as this.

### Database

The database schema is located at `src/main/resources/schema.sql`. In the same directory resides `data.sql` which populates the database with some entries at application initialisation.

The database has three tables:
`books` - basic details of a book
`inventory` - the stock of each book, on a per-type basis
`customer` - a customer's loyalty points

The separate table for `books` and `inventory` is a normalisation to prevent repetition of the fileds in `books`. Furthermore, this breakdown supports the requirement that a book types modifies the underlying cost, i.e., we can calculate an inventory entry's price by quering the `base_price` of its corresponding `books` entry and applying an appropriate modifier.

The data in `customer` is distinct from the other two tables.

The primary keys for `books` and `customer` are `book_id` and `customer_id` respectively. The field `isbn` has not been used as the PK for `books` since it is entirely possible that the store will stock books that use an indexing system other than the ISBN.

The `user_id` has not been used as the PK for `customer_id` since it this application is likely to receive customer details from another service. Furthermore, there is a security consideration here in that an attacker can easily guess a sequentially generated PK over an id generated randomly.

Constraints have been added to `inventory` and `customer` to ensure that stock and loyalty points never drop below 0. 

### Code, big picture

I have followed a conventional code layout for a Spring MVC project. Controller, model and service packages are all present and contain the files you would expect. A properties package imports configuration from `application.properties` and the utils package contains some clasess that are used by the purchase service.

The BookEntity is slightly unusual in that it also includes list elements for Inventory Entities. This simplifies deliver of the GET `/books` api calls but there is a tradeoff in that some dedicated queries were required in the Book and Inventory repositories in order to support POST/PUT/DELETE methods.

In order to deliver this project within the deadline, I have not added any dedicated exception handling or a test suite.

### Some comments on calculateOutcome and the PurchaseTracker

`calculateOutcome` does the majority of work in the PurchaseService. The essential idea is to consider each entry in the `purchaseItems` and `freeItems` arrays in turn and track the implications for cost, loyalty points and stock as we do. While the essential idea is sound, the function as it stands makes an excessive number of database calls. Note that `bookRepository.findByIsbn` is used within both `PurchaseItemModel` loops.

Better would be to total the stock requested within the loops and subsequently make a single call to determine if the inventory can meet the order. However, I had already put a lot of time into this component and I thought it better not to research further. Especially as I do not have a test harness in place.

`BookStock` should also be refactored to use a `book_id`/`type` tuple as a key. I notice dedicated java classes that do this but, again, decided to deliver something that demonstrates the idea, even if the execution is lacking.

### Configuration

Book types are specified within `src/main/resources/application.properties`. The types could have been stored within a dedicated database table with the advantage that a constraint could ensure the integrity of the `inventory`'s `type` field. However, the current approach allows for convenient type creation/modification.

The loyalty points required to claim a free book and the threshold that determines the size of a bundle have also been added to `application.properties` to avoid a proliferation of magic numbers in code.

### Loyalty Point assumptions

The specification says "When 10 loyalty points are accumulated the customer can get one regular or old edition book for free. Once the discount has been applied the loyalty points go back to 0." I've assumed that this means that a customer spends 10 loyalty points to buy a book, not that a customer with, say, 12 loyalty points, has all their loyalty points reset to zero upon claiming a book.

The specification indicates discounts that apply to books "if bought in a bundle of 3 books and more." I've ssumed that books that are claimed using loyalty points do *not* count towards the size of the bundle.

