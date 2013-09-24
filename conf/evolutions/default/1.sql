# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table note (
  id                        bigint not null,
  text                      varchar(255),
  constraint pk_note primary key (id))
;

create table user (
  email                     varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (email))
;

create sequence note_seq;

create sequence user_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists note;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists note_seq;

drop sequence if exists user_seq;

