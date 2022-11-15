--liquibase formatted sql
--changeset Dawid:2
alter table product
    add image varchar(128) after currency;