CREATE TABLE trans_dishes (
    trans_id int references food_transaction(id),
    dish_id int references dish(id)
);

