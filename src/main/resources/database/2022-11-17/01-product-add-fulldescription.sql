--liquibase formatted sql
--changeset Dawid:5
alter table product
    add full_description text default null after description;