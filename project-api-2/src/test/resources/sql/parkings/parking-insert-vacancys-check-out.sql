insert into app_users (id, username, password, role) values (100, 'ana@email.com', '$2a$12$fMGY4VdRbn5.7rvC4ES3QuwaCWydLjlkdC6ZDdU3G46jeJiWCVnyC', 'ROLE_ADMIN');
insert into app_users (id, username, password, role) values (101, 'bia@email.com', '$2a$12$fMGY4VdRbn5.7rvC4ES3QuwaCWydLjlkdC6ZDdU3G46jeJiWCVnyC', 'ROLE_CLIENT');
insert into app_users (id, username, password, role) values (102, 'bob@email.com', '$2a$12$fMGY4VdRbn5.7rvC4ES3QuwaCWydLjlkdC6ZDdU3G46jeJiWCVnyC', 'ROLE_CLIENT');

insert into app_clients (id, cpf, name, id_user) values (11, '05673774583', 'Bianca Lacerda', 101);
insert into app_clients (id, cpf, name, id_user) values (12, '91468050524', 'Bob Brown', 102);

INSERT INTO app_vacancy (id, code, status) values (1000, 'B-20', 'OCUPPIED');
INSERT INTO app_vacancy (id, code, status) values (1001, 'B-30', 'OCUPPIED');
INSERT INTO app_vacancy (id, code, status) values (1002, 'B-40', 'OCUPPIED');
INSERT INTO app_vacancy (id, code, status) values (1003, 'B-50', 'FREE');
INSERT INTO app_vacancy (id, code, status) values (1004, 'B-60', 'FREE');


INSERT INTO app_client_vacancy (number_receipt, plate, mark, model, color, date_enter, date_exit, id_client, id_vacancy)
    values ('20230313-101300', 'FIT-1020', 'FIAT', 'PALIO', 'GREEN', '2023-03-13 10:15:00', '2023-03-13 10:30:00', 11, 1000);
INSERT INTO app_client_vacancy (number_receipt, plate, mark, model, color, date_enter, id_client, id_vacancy)
    values ('20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'WHITE', '2023-03-14 10:15:00', 12, 1001);
INSERT INTO app_client_vacancy (number_receipt, plate, mark, model, color, date_enter, id_client, id_vacancy)
    values ('20230315-101500', 'FIT-1020', 'FIAT', 'PALIO', 'GREEN', '2023-03-14 10:15:00', 11, 1002);


