# Introduction

An implementation of Sporty group's Backend Engineer takehome assignment.

## Running this application

Assuming Java 17 is installed, running the following command should suffice: 
```
mvn spring-boot:run
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
The `mvn spring-boot:run` command will now start the application.

## Usage

### Retrieving the books available for purchase

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


## Design Decisions

- Spring popular framework, 
- H2, handy for prototyping
- distinct table for books v inv, normalisation, prices can be determined from base price of book
- InventoryPK possibly overkill but matches schema
- user_id field in customer table, security, protect db details from attacker
- a note on camel case
