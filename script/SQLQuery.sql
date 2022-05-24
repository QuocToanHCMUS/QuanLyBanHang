INSERT INTO users (store_id, name, birthday, address, username, password)
VALUES ('STORE1', N'Nguyễn Văn A', GETDATE(), 'HCM', 'quanlytrungtam', '1234'),
('STORE1', N'Nguyễn Văn B', GETDATE(), 'HN', 'quanlycuahang1', '1234'),
('STORE1', N'Nguyễn Văn C', GETDATE(), 'DN', 'quanlycuahang2', '1234'),
('STORE1', N'Nguyễn Văn D', GETDATE(), 'DL', 'nhanviencuahang1', '1234'),
('STORE1', N'Nguyễn Văn E', GETDATE(), 'HN', 'nhanviencuahang2', '1234');

-- UPDATE users SET password = '047834ad92f6227c76c2c8c20b88c023', store_id = 'STORE1'  where id = 1;
-- UPDATE users SET password = '9b5e81a9f93bd70ef5f20ca8df003e2d', store_id = 'STORE1' where id = 2;
-- UPDATE users SET password = '4e458c0caf34f031ea59777eb7988c08', store_id = 'STORE1' where id = 3;
-- UPDATE users SET password = 'a7b8ab067a1acd19c682278378a4a8ba', store_id = 'STORE1' where id = 4;
-- UPDATE users SET password = 'b633b2c605f2062802a161645dc6d866', store_id = 'STORE1' where id = 5;

-- DBCC CHECKIDENT (users, RESEED, 0)

INSERT INTO role(role)
VALUES ('QUANLY_TRUNGTAM'),
('QUANLY_CUAHANG'),
('NHAN_VIEN');

INSERT INTO store(id, name, address)
VALUES('STORE1', 'Cua hang 1', 'TPHCM');

INSERT INTO store(id, name, address)
VALUES('STORE2', 'Cua hang 2', 'HN');

INSERT INTO center_manager(user_id) VALUES (1);

INSERT INTO store_manager(user_id) VALUES (2), (3);

INSERT INTO staff(user_id, staff_no, rank) 
VALUES (4, 'NV1', 3), 
(5, 'NV2', 2);

INSERT INTO user_role(role_id, user_id) 
VALUES(1, 1),
(2, 2),
(2, 3),
(3, 2),
(3, 3),
(3, 4),
(3, 5);

insert into work_time(type, start_time, end_time, description)
VALUES(1, '06:00:00', '14:00:00', 'Ca sáng'),
(2, '14:00:00', '22:00:00', 'Ca chiều'),
(3, '22:00:00', '06:00:00', 'Ca đêm');
UPDATE work_time set description = 'Ca sáng' where id = 1;
UPDATE work_time set description = 'Ca chiều' where id = 2;
UPDATE work_time set description = 'Ca đêm' where id = 3;


insert into customer(id, name, birthday, address)
VALUES('CUSTOMER1', 'Khach Hang A', '1985-06-12', 'TP HCM'),
('CUSTOMER2', 'Khach Hang B', '1995-01-30', 'HN');

---- update customer set id = 'CUSTOMER1' WHERE id = 'KH1';
-- update customer set id = 'CUSTOMER2' WHERE id = 'KH2';

insert into invoice(id, customer_id, user_id, sale_date, discount, total_price, discount_price)
VALUES('INVOICE1', 'CUSTOMER1', 4, '2021-12-30 16:30:55', 0, 150.000, 150.000),
('INVOICE2', 'CUSTOMER1', 4, '2021-12-30 15:22:25', 0, 250.000, 250.000),
('INVOICE3', 'CUSTOMER1', 4, '2021-12-30 21:34:22', 0, 50.000, 50.000);

-- update invoice set customer_id = 'CUSTOMER1' WHERE id = 'INVOICE1';
-- update invoice set customer_id = 'CUSTOMER1' WHERE id = 'INVOICE2';
-- update invoice set customer_id = 'CUSTOMER1' WHERE id = 'INVOICE3';

insert into product(id, name, price, import_price)
VALUES('PRODUCT1', 'Bim bim', 5.000, 3.000),
('PRODUCT2', 'Kep toc', 12.000, 10.000),
('PRODUCT3', 'Giay an', 6.500, 5.000),
('PRODUCT4', 'Khan mat', 15.000, 12.000),
('PRODUCT5', 'Mi goi', 10.000, 8.000);

insert into detail_invoice(product_id, invoice_id, quantity, price)
VALUES('PRODUCT1', 'INVOICE1', 10, 50.000),
('PRODUCT2', 'INVOICE1', 5, 60.000),
('PRODUCT5', 'INVOICE1', 4, 40.000),
('PRODUCT4', 'INVOICE2', 10, 150.000),
('PRODUCT3', 'INVOICE2', 10, 65.000),
('PRODUCT5', 'INVOICE2', 3, 35.000),
('PRODUCT1', 'INVOICE3', 10, 50.000);

insert into detail_stock(store_id, product_id, quantity, sell_quantity)
values('STORE1', 'PRODUCT1', 30, 20),
('STORE1', 'PRODUCT2', 40, 5),
('STORE1', 'PRODUCT3', 10, 23),
('STORE1', 'PRODUCT4', 10, 28),
('STORE1', 'PRODUCT5', 7, 50);

insert into import_invoice(id, store_id, store_manager_id, import_invoice_date, total_price)
VALUES('IMPORT_INVOICE1', 'STORE1', 2, '2022-01-03 16:30:55', 150.000),
('IMPORT_INVOICE2', 'STORE1', 2, '2022-01-03 15:22:25', 250.000),
('IMPORT_INVOICE3', 'STORE1', 2, '2022-01-03 21:34:22', 50.000);

insert into detail_import_invoice(import_invoice_id, product_id, quantity, price)
VALUES('IMPORT_INVOICE1', 'PRODUCT1', 10, 50.000),
('IMPORT_INVOICE1','PRODUCT2', 5, 60.000),
('IMPORT_INVOICE1','PRODUCT5', 4, 40.000),
('IMPORT_INVOICE2','PRODUCT4', 10, 150.000),
('IMPORT_INVOICE2','PRODUCT3', 10, 65.000),
('IMPORT_INVOICE2','PRODUCT5', 3, 35.000),
('IMPORT_INVOICE3','PRODUCT1', 10, 50.000);

insert into salary_staff(staff_id, start_time, end_time, salary, created_date, work_date)
VALUES (4, '2022-01-01', '2022-01-31', 5000000, '2022-01-31 16:30:55', 31),
(5, '2022-01-01', '2022-01-31', 4500000, '2022-01-31 16:30:55', 28),
(11, '2022-01-01', '2022-01-31', 4755000, '2022-01-31 16:30:55', 29),
(12, '2022-01-01', '2022-01-31', 3954000, '2022-01-31 16:30:55', 26);