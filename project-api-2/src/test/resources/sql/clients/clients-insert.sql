insert into app_users (id, username, password, role) values (100, 'ana@email.com', '$2a$12$fMGY4VdRbn5.7rvC4ES3QuwaCWydLjlkdC6ZDdU3G46jeJiWCVnyC', 'ROLE_ADMIN');
insert into app_users (id, username, password, role) values (101, 'bia@email.com', '$2a$12$fMGY4VdRbn5.7rvC4ES3QuwaCWydLjlkdC6ZDdU3G46jeJiWCVnyC', 'ROLE_CLIENT');
insert into app_users (id, username, password, role) values (102, 'bob@email.com', '$2a$12$fMGY4VdRbn5.7rvC4ES3QuwaCWydLjlkdC6ZDdU3G46jeJiWCVnyC', 'ROLE_CLIENT');
insert into app_users (id, username, password, role) values (103, 'luis@email.com', '$2a$12$fMGY4VdRbn5.7rvC4ES3QuwaCWydLjlkdC6ZDdU3G46jeJiWCVnyC', 'ROLE_CLIENT');



insert into app_clients (id, cpf, name, id_user) values (11, '05673774583', 'Bianca Lacerda', 101);
insert into app_clients (id, cpf, name, id_user) values (12, '91468050524', 'Bob Brown', 102);
