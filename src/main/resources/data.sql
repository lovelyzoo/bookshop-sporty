-- books
insert into books (title, author, isbn, base_price) values ('The Iliad', 'Emily Wilson', 9781324001805, 20.00);
insert into books (title, author, isbn, base_price) values ('Children of Time', 'Adrian Tchaikovsky', 9781447273301, 10.00);
insert into books (title, author, isbn, base_price) values ('Knife', 'Salman Rushdie', 9780593730249, 12.00);

-- inventory
insert into inventory (book_id, type, stock) values (1, 'N', 3);
insert into inventory (book_id, type, stock) values (1, 'R', 4);
insert into inventory (book_id, type, stock) values (1, 'O', 5);
insert into inventory (book_id, type, stock) values (2, 'N', 7);