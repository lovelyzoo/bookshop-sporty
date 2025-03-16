drop table if exists inventory;
drop table if exists books;

create table books (book_id int AUTO_INCREMENT primary key, title varchar(250), author varchar(250), isbn bigint not null, base_price numeric(20,2) not null, unique(isbn));
create table inventory (book_id int not null, type varchar(1) not null, stock int default 0, foreign key (book_id) references books (book_id), primary key (book_id, type));
alter table inventory add constraint check_stock_positive check (stock >= 0);

drop table if exists customer;
create table customer (customer_id int AUTO_INCREMENT primary key, user_id varchar(250), loyalty_points bigint default 0);
alter table customer add constraint check_loyalty_points_positive check (loyalty_points >= 0);