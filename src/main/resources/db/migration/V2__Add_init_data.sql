insert into user_details (id, account_status, created_at, password, phone_number, status, username)
values (1, 'activated', current_timestamp, '1234', '1111111111', 'available', 'peter');

insert into user_role (user_id, roles)
values (1, 'USER');

insert into user_details (id, account_status, created_at, password, phone_number, status, username)
values (2, 'activated', current_timestamp, '1234', '2222222222', 'available', 'ivan');

insert into user_role (user_id, roles)
values (2, 'USER'), (2, 'ADMIN');

insert into user_details (id, account_status, created_at, password, phone_number, status, username)
values (3, 'activated', current_timestamp, '1234', '3333333333', 'available', 'pumba');

insert into user_role (user_id, roles)
values (3, 'DEV'), (3, 'USER');

update user_details set password='$2a$08$PnBNTt8RILQFwMDgX2IOu.Sz5/omfP6QtaFzM1LpJ9NCHoraXuH76' where id=1;
update user_details set password='$2a$08$Wv6r0.s9y9cz8xLxVLJDFe.BeOEWsNC4i8IqGF7/2zvNeFkqZ4nF2' where id=2;
update user_details set password='$2a$08$VoaZEY72y264v2BEtQ0E.uBPRYMYQ0MBMT0fl9mKyh85M.xnlSZRS' where id=2;

insert into conversation (id, created_at, recipient_id, sender_id)
values (1, current_timestamp, 1, 2);

insert into message (id, body, created_at, conversation_id, recipient_id, sender_id)
values (1, 'test message body', current_timestamp, 1, 1, 2);