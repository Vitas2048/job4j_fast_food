CREATE TABLE db_notification (
    id serial primary key not null,
    notice_text text,
    customer_id int,
    create_time timestamp
);