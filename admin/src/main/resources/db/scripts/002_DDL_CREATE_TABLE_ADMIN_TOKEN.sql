CREATE TABLE IF NOT EXISTS admin_token (
    id serial primary key,
    access_token text,
    refresh_token text,
    admin_id int references food_admin(id)
);