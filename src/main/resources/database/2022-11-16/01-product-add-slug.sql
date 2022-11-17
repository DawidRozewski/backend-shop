--liquibase formatted sql
--changeset Dawid:3
alter table product
    add slug varchar(255) after image;