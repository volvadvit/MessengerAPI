create table hibernate_sequence (next_val bigint);
insert into hibernate_sequence values ( 2 );

create table conversation (
  id bigint not null,
  created_at timestamp,
  recipient_id bigint not null,
  sender_id bigint not null,
  primary key (id)
);

create table message (
  id bigint not null,
  body varchar(2048),
  created_at timestamp,
  conversation_id bigint not null,
  recipient_id bigint not null,
  sender_id bigint not null,
  primary key (id)
);

create table user_details (
  id bigint not null,
  account_status varchar(255),
  created_at timestamp,
  password varchar(255),
  phone_number varchar(255),
  status varchar(255),
  username varchar(255),
  primary key (id)
);

create table user_role (
  user_id bigint not null,
  roles varchar(255)
);


alter table user_details add constraint user_details_username_unique unique (username);
alter table conversation add constraint convers_user_recip_fk foreign key (recipient_id) references user_details (id);
alter table conversation add constraint convers_user_send_fk foreign key (sender_id) references user_details (id);
alter table message add constraint message_convers_fk foreign key (conversation_id) references conversation (id);
alter table message add constraint message_recip_fk foreign key (recipient_id) references user_details (id);
alter table message add constraint message_send_fk foreign key (sender_id) references user_details (id);
alter table user_role add constraint user_role_fk foreign key (user_id) references user_details (id);

