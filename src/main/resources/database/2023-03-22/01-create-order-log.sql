--liquibase formatted sql
--changeset Dawid:19
create table order_log(
    id bigint not null auto_increment PRIMARY KEY,
    order_id bigint not null,
    created datetime not null,
    note text
);