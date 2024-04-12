INSERT INTO app_vacancy (id, code, status) values (1000, 'B-20', 'FREE');
INSERT INTO app_vacancy (id, code, status) values (1001, 'B-30', 'FREE');
INSERT INTO app_vacancy (id, code, status) values (1002, 'B-40', 'OCUPPIED');

insert into app_users (id, username, password, role) values (100, 'ana@email.com', '$2a$12$fMGY4VdRbn5.7rvC4ES3QuwaCWydLjlkdC6ZDdU3G46jeJiWCVnyC', 'ROLE_ADMIN');
insert into app_users (id, username, password, role) values (101, 'bia@email.com', '$2a$12$fMGY4VdRbn5.7rvC4ES3QuwaCWydLjlkdC6ZDdU3G46jeJiWCVnyC', 'ROLE_CLIENT');
insert into app_users (id, username, password, role) values (102, 'bob@email.com', '$2a$12$fMGY4VdRbn5.7rvC4ES3QuwaCWydLjlkdC6ZDdU3G46jeJiWCVnyC', 'ROLE_CLIENT');