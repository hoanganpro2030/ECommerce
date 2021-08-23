INSERT INTO EMPLOYEE (VISA, FIRST_NAME, LAST_NAME, BIRTH_DAY, VERSION)
VALUES
    ('ADG', 'DANG','HOANG AN','2020-04-20', 1),
    ('ABC', 'NGUYEN','A B','2020-04-20', 2),
    ('DEF', 'DOAN','DU','2020-04-20', 3),
    ('KNT', 'Nguyen', 'Thanh Khoa', '1998-11-20',1),
    ('HVV', 'Vu', 'Van Huynh', '1997-11-20',1),
    ('DTV', 'Truong', 'Le Viet Danh', '1998-11-20',1),
    ('GTN', 'Nguyen', 'Dang Tuan', '1995-06-10',1);


INSERT INTO "group" (GROUP_LEADER_ID, VERSION)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 1),
    (5, 1),
    (6, 1),
    (7, 1);

INSERT INTO PROJECT (NAME, PROJECT_NUMBER, CUSTOMER, STATUS, START_DATE, END_DATE, VERSION, "group_id")
VALUES
    ('EFV AB', 123,'Peter Parker','NEW','2020-04-20','2020-04-21', 123,1),
    ('EFA CD', 124,'Emma Watson','PLA','2020-04-20','2020-04-21', 124,2),
    ('EFB FV', 125,'Rafel Nadal','PLA','2020-04-20','2020-04-21', 125,3),
    ('OBD', 126,'Zalatan Ibrahimovic','NEW','2020-04-20','2020-04-21', 125,4),
    ('WMS', 127,'Ricardo Kaka','PLA','2020-04-20','2020-04-21', 125,5),
    ('SRM', 128,'Alexander Pato','FIN','2020-04-20','2020-04-21', 125,6),
    ('ETON', 129,'Donaruma','NEW','2020-04-20','2020-04-21', 125,7),
    ('VNPAY', 130,'Uchiha Itachi','PLA','2020-04-20','2020-04-21', 125,6),
    ('VNSHOP', 131,'Hakan Cahanoglu','PLA','2020-04-20','2020-04-21', 125,4),
    ('OM', 132,'Rafel Leao','NEW','2020-04-20','2020-04-21', 125,3);

INSERT INTO PROJECT ("group_id", PROJECT_NUMBER, NAME, CUSTOMER, STATUS, START_DATE, VERSION)
VALUES
    (1, 5642, 'COOTN Dress', 'Customer 6', 'PLA', '2020-08-20', 1),
    (2, 7657, 'PIMPIM OSHI', 'Customer 7', 'PLA', '2020-08-20', 1),
    (3, 3563, 'VEGETERIAN', 'Customer 8', 'FIN', '2020-08-20', 1),
    (1, 7636, 'PIPE WTA', 'Customer 9', 'NEW', '2020-08-20', 1),
    (2, 7363, 'ZOO ZOO', 'Customer 10', 'INP', '2020-08-20', 1),
    (3, 8768, 'Plan Tree On The Earth', 'Customer 11', 'FIN', '2020-08-20', 1),
    (1, 8884, 'Travelling AMM', 'Customer 12', 'NEW', '2020-08-20', 1);


INSERT INTO PROJECT_EMPLOYEE (PROJECT_ID, EMPLOYEE_ID)
VALUES
    (1, 1),
    (1, 2),
    (2, 4),
    (2, 2),
    (3, 3),
    (3, 4),
    (4, 5),
    (4, 6),
    (5, 7),
    (5, 4),
    (6, 3),
    (6, 7),
    (7, 6),
    (8, 1),
    (8, 3),
    (9, 4),
    (9, 5);

INSERT INTO PRODUCT (NAME, DESCRIPTION, price, quantity, image, date_Stock_In, VERSION)
VALUES
('BANH CUON', 'AN DO RAT NGON', 15000, 5, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TRANG', 'AN DO RAT TRANG', 16000,6, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TET', 'AN DO RAT TET', 17000,7, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH IT', 'AN DO RAT IT', 18000,8, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH BEO', 'AN DO RAT BEO', 19000,9, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH kim chi', 'AN DO RAT NGON', 15000, 5, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TRANG 1', 'AN DO RAT TRANG', 16000,6, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TET 2', 'AN DO RAT TET', 17000,7, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH IT 3', 'AN DO RAT IT', 18000,8, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH BEO 4', 'AN DO RAT BEO', 19000,9, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH CUON 5', 'AN DO RAT NGON', 15000, 5, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TRANG 6', 'AN DO RAT TRANG', 16000,6, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TET 7', 'AN DO RAT TET', 17000,7, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH IT 8', 'AN DO RAT IT', 18000,8, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH BEO 9', 'AN DO RAT BEO', 19000,9, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH CUON 10', 'AN DO RAT NGON', 15000, 5, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TRANG 11', 'AN DO RAT TRANG', 16000,6, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TET 12', 'AN DO RAT TET', 17000,7, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH IT 13', 'AN DO RAT IT', 18000,8, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH BEO 14', 'AN DO RAT BEO', 19000,9, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH CUON 15', 'AN DO RAT NGON', 15000, 5, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TRANG 16', 'AN DO RAT TRANG', 16000,6, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TET 17', 'AN DO RAT TET', 17000,7, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH IT 28', 'AN DO RAT IT', 18000,8, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH BEO 29', 'AN DO RAT BEO', 19000,9, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH CUON 30', 'AN DO RAT NGON', 15000, 5, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TRANG 31', 'AN DO RAT TRANG', 16000,6, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TET 32', 'AN DO RAT TET', 17000,7, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH IT 33', 'AN DO RAT IT', 18000,8, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH BEO 34', 'AN DO RAT BEO', 19000,9, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH CUON 35', 'AN DO RAT NGON', 15000, 5, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TRANG 36', 'AN DO RAT TRANG', 16000,6, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH TET 37', 'AN DO RAT TET', 17000,7, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH IT 38', 'AN DO RAT IT', 18000,8, 'ABCXYZ.ORG', '2020-04-20', 1),
('BANH BEO 39', 'AN DO RAT BEO', 19000,9, 'ABCXYZ.ORG', '2020-04-20', 1),



