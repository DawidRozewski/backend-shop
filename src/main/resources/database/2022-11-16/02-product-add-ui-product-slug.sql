--liquibase formatted sql
--changeset Dawid:4
alter table product
    add constraint ui_product_slug unique key (slug);