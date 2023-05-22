package model;

public class Topics {
    public static final String TOPIC_GET_NOTICE = "get_notification";

    public static final String TOPIC_DO_NOTICE = "to_notification";

    public static final String TOPIC_DELIVERING = "order_delivering";

    public static final String TOPIC_COMPLETED_FROM_KITCHEN = "order_completed_by_kitchen";

    public static final String TOPIC_TO_DELIVERY_SERVICE = "order_to_delivery";

    public static final String TOPIC_COMPLETED = "order_completed";

    public static final String TOPIC_DENIED = "order_denied";

    public static final String TOPIC_ALL_DISHES_ADMIN = "all_dishes";

    public static final String TOPIC_ALL_DISHES_DELIVERY = "all_dishes_";

    public static final String TOPIC_ALL_DISHES_KITCHEN = "all_dishes_1";

    public static final String TOPIC_ALL_DISHES_ORDER = "all_dishes_2";

    public static final String TOPIC_ALL_DISHES_PAYMENT = "all_dishes_3";

    public static final String TOPIC_SEND_DISH_ADMIN = "send_dish";

    public static final String TOPIC_SEND_DISH_DELIVERY = "send_dish_";

    public static final String TOPIC_SEND_DISH_KITCHEN = "send_dish_1";

    public static final String TOPIC_SEND_DISH_ORDER = "send_dish_2";

    public static final String TOPIC_SEND_DISH_PAYMENT = "send_dish_3";

    public static final String TOPIC_CREATE_CUSTOMERS_BALANCE = "new_customer_balance";

    public static final String TOPIC_GET_BALANCE_PAYMENT = "get-balance-by-payment";

    public static final String TOPIC_GET_INCOME = "get_income";

    public static final String TOPIC_DO_TRANSACTION = "do_transaction";

    public static final String TOPIC_ORDERS_TO_KITCHEN = "orders_to_kitchen";

    public static final String TOPIC_PAYMENT_RESULT = "payment_result";

    public static final String TOPIC_TOP_UP_BALANCE = "payment_top_up";

    public static final String TOPIC_REMOVE_DISH = "admin_remove_dish";

    public static final String TOPIC_REMOVE_DISH_DELIVERY = "admin_remove_dish_delivery";

    public static final String TOPIC_REMOVE_DISH_KITCHEN = "admin_remove_dish_kitchen";

    public static final String TOPIC_REMOVE_DISH_ORDER = "admin_remove_dish_order";

    public static final String TOPIC_REMOVE_DISH_PAYMENT = "admin_remove_dish_payment";

    public static final String TOPIC_REMOVE_CUSTOMER = "admin_remove_customer";

    public static final String TOPIC_GET_CUSTOMER_ADMIN = "admin_get_customer";

    public final static int STATUS_CREATED = 1;

    public final static int STATUS_PROCESSING = 2;

    public final static int STATUS_COMPLETED = 3;

    public final static int STATUS_DELIVERING = 4;

    public final static int STATUS_DENIED = 5;
}
