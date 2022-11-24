--liquibase formatted sql
--changeset Dawid:11
alter table review
    add moderated boolean default false;

