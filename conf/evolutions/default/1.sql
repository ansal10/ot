# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table option (
  id                        bigserial not null,
  question_id               bigint,
  option                    varchar(10240),
  correct                   boolean,
  constraint pk_option primary key (id))
;

create table permission (
  id                        bigserial not null,
  users_id                  bigint,
  permission                integer,
  constraint ck_permission_permission check (permission in (0,1,2,3,4,5,6,7)),
  constraint pk_permission primary key (id))
;

create table question (
  id                        bigserial not null,
  question                  varchar(10240),
  constraint uq_question_question unique (question),
  constraint pk_question primary key (id))
;

create table result (
  id                        bigserial not null,
  users_id                  bigint,
  test_id                   bigint,
  response                  varchar(102400),
  started_on                timestamp,
  submitted_on              timestamp,
  ends_on                   timestamp,
  correct_answer            bigint,
  wrong_answer              bigint,
  un_attempted              bigint,
  correct_marks             float,
  wrong_marks               float,
  constraint pk_result primary key (id))
;

create table test (
  id                        bigserial not null,
  name                      varchar(255),
  created_on                timestamp,
  expired_on                timestamp,
  type                      integer,
  durations                 bigint,
  total_questions           bigint,
  is_active                 boolean,
  total_marks               float,
  constraint ck_test_type check (type in (0,1,2,3)),
  constraint pk_test primary key (id))
;

create table test_questions (
  id                        bigserial not null,
  test_id                   bigint,
  question_id               bigint,
  correct_answer_mark       float,
  wrong_answer_mark         float,
  constraint pk_test_questions primary key (id))
;

create table users (
  id                        bigserial not null,
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

alter table option add constraint fk_option_question_1 foreign key (question_id) references question (id);
create index ix_option_question_1 on option (question_id);
alter table permission add constraint fk_permission_users_2 foreign key (users_id) references users (id);
create index ix_permission_users_2 on permission (users_id);
alter table result add constraint fk_result_users_3 foreign key (users_id) references users (id);
create index ix_result_users_3 on result (users_id);
alter table result add constraint fk_result_test_4 foreign key (test_id) references test (id);
create index ix_result_test_4 on result (test_id);
alter table test_questions add constraint fk_test_questions_test_5 foreign key (test_id) references test (id);
create index ix_test_questions_test_5 on test_questions (test_id);
alter table test_questions add constraint fk_test_questions_question_6 foreign key (question_id) references question (id);
create index ix_test_questions_question_6 on test_questions (question_id);



# --- !Downs

drop table if exists option cascade;

drop table if exists permission cascade;

drop table if exists question cascade;

drop table if exists result cascade;

drop table if exists test cascade;

drop table if exists test_questions cascade;

drop table if exists users cascade;

