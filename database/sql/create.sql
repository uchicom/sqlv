create table sample(
id bigint auto_increment,
updated varchar(256),
update_datetime timestamp,
update_seq integer,
inserted varchar(256) not null,
insert_datetime timestamp not null,
name varchar(128),
memo varchar(1024),
primary key(id)
);