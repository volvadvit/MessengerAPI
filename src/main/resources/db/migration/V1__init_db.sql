create table hibernate_sequence (next_val bigint);
insert into hibernate_sequence values ( 2 );

create table conversation (
  id bigint not null,
  created_at timestamp,
  name varchar(255),
  photo_url varchar(255),
  primary key (id)
);

create table message (
  id bigint not null,
  body varchar(2048),
  created_at timestamp,
  status varchar(255),
  conversation_id bigint not null,
  sender_id bigint not null,
  primary key (id)
);

create table user_details (
  id bigint not null,
  created_at timestamp,
  last_active timestamp,
  password varchar(60),
  email varchar(255),
  status varchar(255),
  username varchar(255),
  photo_url varchar(255),
  primary key (id)
);

create table user_friends (
  user_id bigint not null,
  friend_id bigint
);

create table user_role (
  user_id bigint not null,
  roles varchar(255)
);

create table user_conversations (
  user_id bigint not null,
  conversation_id bigint not null
);

alter table user_details add constraint user_details_username_unique unique (username);
alter table user_conversations add constraint convers_id foreign key (conversation_id) references conversation (id);
alter table user_conversations add constraint convers_user foreign key (user_id) references user_details (id);
alter table message add constraint message_convers_fk foreign key (conversation_id) references conversation (id);
alter table message add constraint message_send_fk foreign key (sender_id) references user_details (id);
alter table user_role add constraint user_role_fk foreign key (user_id) references user_details (id);
alter table user_friends add constraint user_id_fk foreign key (user_id) references user_details (id);

