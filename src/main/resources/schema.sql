drop table if exists books;
create table books (book_id int AUTO_INCREMENT primary key, title varchar(250), author varchar(250), isbn bigint not null, base_price numeric(20,2) not null, unique(isbn));