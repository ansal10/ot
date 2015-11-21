# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table option (
  question_id               bigint,
  option                    varchar(10240))
;

create table permission (
  user_id                   bigint,
  permission                varchar(255))
;

create table question (
  id                        bigint not null,
  question                  varchar(10240),
  correct_option            bigint,
  correct_answer            varchar(255),
  constraint uq_question_question unique (question),
  constraint pk_question primary key (id))
;

create table test (
  id                        bigint not null,
  name                      varchar(255),
  created_on                timestamp,
  expired_on                timestamp,
  type                      integer,
  total_questions           bigint,
  is_active                 boolean,
  total_marks               bigint,
  constraint ck_test_type check (type in (0,1,2,3)),
  constraint pk_test primary key (id))
;

create table test_questions (
  test_id                   bigint,
  question_id               bigint,
  correct_answer_mark       bigint,
  wrong_answer_mark         bigint)
;

create table users (
  id                        bigint not null,
  username                  varchar(255),
  username_hash             varchar(256),
  password_hash             varchar(256),
  password_salt             varchar(512),
  active                    boolean,
  super_user                boolean,
  created_on                timestamp,
  password_expired_on       timestamp,
  email                     varchar(255),
  constraint uq_users_username unique (username),
  constraint pk_users primary key (id))
;


create table test_users (
  test_id                        bigint not null,
  users_id                       bigint not null,
  constraint pk_test_users primary key (test_id, users_id))
;
create sequence question_seq;

create sequence test_seq;

create sequence users_seq;

alter table option add constraint fk_option_question_1 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_option_question_1 on option (question_id);
alter table permission add constraint fk_permission_user_2 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_permission_user_2 on permission (user_id);
alter table test_questions add constraint fk_test_questions_test_3 foreign key (test_id) references test (id) on delete restrict on update restrict;
create index ix_test_questions_test_3 on test_questions (test_id);
alter table test_questions add constraint fk_test_questions_question_4 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_test_questions_question_4 on test_questions (question_id);



alter table test_users add constraint fk_test_users_test_01 foreign key (test_id) references test (id) on delete restrict on update restrict;

alter table test_users add constraint fk_test_users_users_02 foreign key (users_id) references users (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists option;

drop table if exists permission;

drop table if exists question;

drop table if exists test;

drop table if exists test_users;

drop table if exists test_questions;

drop table if exists users;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists question_seq;

drop sequence if exists test_seq;

drop sequence if exists users_seq;

