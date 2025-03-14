# Introduction

An implementation of Sporty group's Backend Engineer takehome assignment.

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

### Returning the books available for purchase

#### Individual book
GET `/books/{isbn}`:
```
$ curl -X GET http://localhost:8080/books/9781447273301
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
```
curl -H "Content-Type: application/json" -X GET --data '{"userId": "EORIZQA123", "purchaseItems": [{"isbn": "9781324001805", "type": "N", "quantity": "2"}], "freeItems": []}' http://localhost:8080/purchase
```

## Design Decisions

- Spring popular framework, 
- H2, handy for prototyping
- distinct table for books v inv, normalisation, prices can be determined from base price of book
- InventoryPK possibly overkill but matches schema
- user_id field in customer table, security, protect db details from attacker
- a note on camel case within the db
- Big Decimal
