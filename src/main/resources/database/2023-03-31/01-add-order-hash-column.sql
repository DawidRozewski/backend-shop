--liquibase formatted sql
--changeset Dawid:29
alter table `order` add order_hash varchar(12);