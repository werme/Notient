# --- !Ups

create table note (
  id                        bigint not null,
  title                     varchar(255),
  text                      varchar(255),
  constraint pk_note primary key (id))
;

create sequence note_seq;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists note;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists note_seq;
