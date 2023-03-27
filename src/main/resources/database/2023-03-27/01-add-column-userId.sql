--liquibase formatted sql
--changeset Dawid:24
alter table `order` add user_id bigint;
--changeset Dawid:25
alter table `order` add constraint fk_order_user_id foreign key (user_id) references users(id);