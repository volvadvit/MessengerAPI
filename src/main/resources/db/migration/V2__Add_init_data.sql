insert into user_details (id, account_status, created_at, password, phone_number, status, username)
values (1, 'activated', current_timestamp, '123', '1111111111', 'available', 'first');

insert into user_details (id, account_status, created_at, password, phone_number, status, username)
values (2, 'activated', current_timestamp, '123', '1111111111', 'available', 'second');

update user_details set password='$2a$08$y/Z9QQva7FrCfwSs.BOZJelYXk7/toea8HBI3ZWwkq45nJYrcP9NW' where id=1;
update user_details set password='$2a$08$wZ./K9BwgP5oFzLp9E87ZeNBAZuFcbJrtjLXejeFEI0iRBRyP0iTy' where id=2;

insert into conversation (id, created_at, recipient_id, sender_id)
values (1, current_timestamp, 1, 2);

insert into message (id, body, created_at, conversation_id, recipient_id, sender_id)
values (1, 'test message body', current_timestamp, 1, 1, 2);