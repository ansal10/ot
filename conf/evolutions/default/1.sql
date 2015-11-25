# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table option (
  id                        bigint not null,
  question_id               bigint,
  option                    varchar(10240),
  correct                   boolean,
  constraint pk_option primary key (id))
;

create table permission (
  id                        bigint not null,
  users_id                  bigint,
  permission                integer,
  constraint ck_permission_permission check (permission in (0,1,2,3,4,5,6,7)),
  constraint pk_permission primary key (id))
;

create table question (
  id                        bigint not null,
  question                  varchar(10240),
  constraint uq_question_question unique (question),
  constraint pk_question primary key (id))
;

create table result (
  id                        bigint not null,
  users_id                  bigint,
  test_id                   bigint,
  response                  varchar(102400),
  started_on                timestamp,
  submitted_on              timestamp,
  ends_on                   timestamp,
  correct_answer            bigint,
  wrong_answer              bigint,
  un_attempted              bigint,
  correct_marks             double,
  wrong_marks               double,
  constraint pk_result primary key (id))
;

create table test (
  id                        bigint not null,
  name                      varchar(255),
  created_on                timestamp,
  expired_on                timestamp,
  type                      integer,
  durations                 bigint,
  total_questions           bigint,
  is_active                 boolean,
  total_marks               double,
  constraint ck_test_type check (type in (0,1,2,3)),
  constraint pk_test primary key (id))
;

create table test_questions (
  id                        bigint not null,
  test_id                   bigint,
  question_id               bigint,
  correct_answer_mark       double,
  wrong_answer_mark         double,
  constraint pk_test_questions primary key (id))
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

create sequence option_seq;

create sequence permission_seq;

create sequence question_seq;

create sequence result_seq;

create sequence test_seq;

create sequence test_questions_seq;

create sequence users_seq;

alter table option add constraint fk_option_question_1 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_option_question_1 on option (question_id);
alter table permission add constraint fk_permission_users_2 foreign key (users_id) references users (id) on delete restrict on update restrict;
create index ix_permission_users_2 on permission (users_id);
alter table result add constraint fk_result_users_3 foreign key (users_id) references users (id) on delete restrict on update restrict;
create index ix_result_users_3 on result (users_id);
alter table result add constraint fk_result_test_4 foreign key (test_id) references test (id) on delete restrict on update restrict;
create index ix_result_test_4 on result (test_id);
alter table test_questions add constraint fk_test_questions_test_5 foreign key (test_id) references test (id) on delete restrict on update restrict;
create index ix_test_questions_test_5 on test_questions (test_id);
alter table test_questions add constraint fk_test_questions_question_6 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_test_questions_question_6 on test_questions (question_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists option;

drop table if exists permission;

drop table if exists question;

drop table if exists result;

drop table if exists test;

drop table if exists test_questions;

drop table if exists users;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists option_seq;

drop sequence if exists permission_seq;

drop sequence if exists question_seq;

drop sequence if exists result_seq;

drop sequence if exists test_seq;

drop sequence if exists test_questions_seq;

drop sequence if exists users_seq;

