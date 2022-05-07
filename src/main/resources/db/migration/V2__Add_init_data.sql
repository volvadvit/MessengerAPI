insert into user_details (id, created_at, password, email, status, username, photo_url, last_active)
values
  (1, current_timestamp, '1234', 'user@email.com', 'activated', 'peter', '', current_timestamp),
  (2, current_timestamp, '1234', 'admin@email.com', 'activated', 'ivan', '', current_timestamp),
  (3, current_timestamp, '1234', 'simba@email.com', 'activated', 'pumba', '', current_timestamp);

insert into user_role (user_id, roles)
values
  (1, 'USER'),
  (2, 'USER'),
  (2, 'ADMIN'),
  (3, 'USER');

insert into user_friends (user_id, friend_id)
values
  (1, 3),
  (3, 1),
  (2, 1);

update user_details set password='$2a$08$PnBNTt8RILQFwMDgX2IOu.Sz5/omfP6QtaFzM1LpJ9NCHoraXuH76' where id=1;
update user_details set password='$2a$08$Wv6r0.s9y9cz8xLxVLJDFe.BeOEWsNC4i8IqGF7/2zvNeFkqZ4nF2' where id=2;
update user_details set password='$2a$08$VoaZEY72y264v2BEtQ0E.uBPRYMYQ0MBMT0fl9mKyh85M.xnlSZRS' where id=3;

insert into conversation (id, created_at, name, photo_url)
values
  (1, current_timestamp, 'CHat', ''),
  (2, current_timestamp, 'traDe', '');

insert into user_conversations (user_id, conversation_id)
values
  (1, 1),
  (2, 1),
  (3, 1),
  (1, 2),
  (2, 2);

insert into message (id, body, created_at, conversation_id, sender_id, status)
values
  (1, 'test message body', current_timestamp, 1, 1, 'SENT'),
  (2, 'for CHat test body message test text', current_timestamp, 1, 2, 'SENT'),
  (3, 'SHALALALALALLALALALA BLALALALALAALAALLALL TRAATATTATATATATTATTAT skgjsgnsjkgngj jwnejfnwjej  jwj j. LKEAfLFK????/kfwelmk mm 234  mm km2 3m  k3m', current_timestamp, 1, 3, 'SENT'),
  (4, 'test message body new', current_timestamp, 2, 1, 'SENT');