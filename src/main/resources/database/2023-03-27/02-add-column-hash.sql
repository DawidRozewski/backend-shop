--liquibase formatted sql
--changeset Dawid:26
alter table users add hash varchar(120);
--changeset Dawid:27
alter table users add hash_date datetime;