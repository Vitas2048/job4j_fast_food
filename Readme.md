# job4j_fast_food  Проект FastFood (Сервис по доставке еды)

### Использованные технологии

* Spring Framework 2.7.3
* PostgresSQL 42.5.1
* Liquibase 4.15.0
* Lombok 1.18.26
* Apache Kafka 
* Collections Framework
* Apache Kafka
* Docker
* Java JWT
* Java Persistence API

### Требование к окружению

* Docker
* Docker-compose
* Postman
* Java 17
* PostgresSQL 15
* Apache maven 3.8

### Структура проекта 

Проект представляет собой RESTful приложение с микросервисной архитектурой. 
Приложение разработано для сервиса по доставке еды, разбитое на микросервисы:
* Администрация (Admin)  
Разработан для контроля и управления всем сервисом. 
Позволяет удалять пользователей и блюда из меню прямиком из базы данных, 
также имеется возможность рассчитать прибыль за день. Изначально в базе данных имеется
администратор с заранее заданными данными, имеется возможность регистрации 
нового администратора.
* Доставка (Delivery)  
Разработан для контроля процесса доставки, сервис обрабатывает входящий заказ, 
поступивший с сервиса Kitchen, уведомляет о начале доставки и завершении 
заказа/доставки
* Блюда (Dish)
Разработана для корректировки меню сервиса по доставке еды, 
сервис может удалять и создавать новые блюда в меню.
* Домены/модели (Domain)  
Разработан для создания основных моделей, используемых во всем сервисе.
* Кухня (Kitchen)   
Разработан для контроля процесса приготовления блюда, сервис обрабатывает входящий заказ ,
поступивший из сервиса Order, сервис отправляет уведомление о том, что заказ находится 
в процессе готовки, также если на кухне не будет необходимых продуктов для выполнения 
заказа, сервис может отклонить запрос. После приготовления сервис отправляет уведомление 
пользователю и сервису доставки еды.
* Уведомления (Notification)  
Разработан для отправки уведомлений пользователю.
* Заказы (Order)  
Разработан для взаимодействия между пользователем и сервисом. Реализована функция регистрации 
нового пользователя. Пользователь может проверить свой баланс, список заказов, уведомления
по id можно будет выббрать отдельный заказ и узнать подробности. Пользователь формирует заказ 
и сервис при успешной оплате автоматически отправляет заказ в сервис Kitchen, 
при отказе оплаты заказ отменяется, меняя свой статус.
* Оплата (Payment)  
Разработан для осуществления денежных транзакций и проверки баланса. При регистрации
нового пользователя сервис автоматически начисляет начальный баланс пользователя 
равный 5000 (для простоты демонстрации). Если пользователю от сервиса Kitchen приходит отказ,
сервис возвращает деньги и завершает заказ.
1) Сервис администрации, 
### Запуск проекта 
1. Установить JDK 17
2. Установить Postman
3. Установить Maven
4. Установить Docker
5. Установить Docker-compose
6. Открыть терминал и перейти к папке проекта
7. Выполнить команду 
```shell
 mvn package
```
![package](files/package.png)
8. Выполнить команду
```shell
 docker-compose build
```
9. Выполнить команду
```shell
 docker-compose build
```
## Взаимодействие с приложением

### Сервис Order

URL сервиса: http://localhost:8080

1. Для того, чтобы зарегистрировать нового пользователя небходимо - отправить POST запрос
в программе Postman (в дальнейшем отправить запрос) по ${URL_ORDER}/api/registration,
в BODY указать JSON пользователя:  
![custom_registration](files/custom_reg.png)
2. Для получения уникального токена необходимо авторизироваться, отправив POST запрос 
по ${URL_ORDER}/api/login также указав в BODY JSON полльзователя:  
![custom_auth](files/custom_auth.png)
3. Получив токен, пользователь может использвать все функции сервиса, для авторизации
необходимо в запросах указать в HEADER ключ 'Authorization' и в значении указать 
'Bearer ${Token}'  
![token_auth](files/token_auth.png)
4. Для проверки баланса необходимо - отправить GET запрос по 
${URL_ORDER}/api/customer/get-balance:  
![customer_balance](files/custom_balance.png)
5. Для пополнения - отправить POST запрос по
   ${URL_ORDER}/api/customer/get-balance с JSON объектом пополнения: 
![top_up](files/top_up.png)  
![customer_balance_1](files/customer_balance_1.png)
6. Для просмотра доступных блюд для заказа - отправить GET запрос по
${URL_ORDER}/api/order/get-dishes:    
![order_dishes](files/order_dishes.png)
7. Для создания заказа - отправить POST запрос по ${URL_ORDER}/api/order/create-order,
указав в BODY JSON, содержащий Id блюд, в ответе придет JSON c подробным содержанием
заказа:   
![order_create](files/create_order.png)  
8. Для просмотра всех уведомлений отправить GET запрос по
${URL_ORDER}/api/customer/get-notifications:  
![notifications](files/order_notification.png)  
9. В случае отмены заказа от сервиса Kitchen заказ завершается и деньги возвращаются 
пользователю: 
![notification_refund](files/refund.png)
10. В случае отклонения транкзакции, сервис отправит уведомление и отменит заказ:  
![order_denied](files/order_denied.png)
### Сервис Dish

URL сервиса: http://localhost:8081

1. Для добавления нового блюда - отправить POST запрос по ${URL_DISH}/api/dish/add-dish,
указав в BODY JSON:  
![dish_add](files/dish_add.png)  
Новое блюдо отправляется другим сервисам:  
![dishes_customer](files/customer_dishes1.png)
2. Для просмотра всего меню - отправить GET запрос по ${URL_DISH}/api/dish/get-all:  
![dishes_all](files/dishes_all.png)

### Сервис Kitchen

URL сервиса: http://localhost:8082

1. Чтобы проверить какой заказ находится на очереди, необходимо 
отправить GET запрос по ${URL_KITCHEN}/api/kitchen/peek-order:  
![peek_kitchen](files/peek_kitchen.png)
2. Чтобы отправить заказ далее в сервис доставки необходимо отправить POST запрос с 
пустым BODY (т.к. заказы отправляются по очереди) по 
${URL_KITCHEN}/api/kitchen/get-order/to-delivery:  
![send_to_DELIVERY](files/send_to_delivery.png)
3. Для отклонения заказа, необходимо отправить POST запрос с
пустым BODY по ${URL_KITCHEN}/api/kitchen/get-order/deny:  
![kitchen_deny](files/kitchen_deny.png)

### Сервис Delivery

URL сервиса: http://localhost:8083

1. Чтобы проверить какой заказ находится на очереди, необходимо
отправить GET запрос по ${URL_DELIVERY}/api/delivery/get-order/start-delivery: 
![delivery_check](files/deliver_peek.png)
2. Для того, чтобы оповестить пользователя о старте доставки, необходимо 
отправить POST запрос с пустым BODY по 
${URL_DELIVERY}/api/delivery/get-order/start-delivery:  
![delivery_start](files/delivery_start.png)
3. Для того, чтобы оповестить пользователя об окончании доставки, необходимо
отправить POST запрос с пустым BODY по
${URL_DELIVERY}/api/delivery/get-order/complete:
![delivery_complete](files/delivery_complete.png)

### Сервис Admin

URL сервиса: http://localhost:8084

* В сервисе Admin имеется предустановленный аккаунт:  
login - admin  
password - password  
1. Для того, чтобы зарегистрировать нового пользователя небходимо - отправить POST запрос
по ${URL_ADMIN}/api/registration в BODY указать JSON аккаунта:  
![admin_reg](files/admin_reg.png)  
![admin_auth](files/admin_auth.png)  
2. Для получения всех блюд в меню, неодбходимо
отправить GET запрос по ${URL_ADMIN}/api/account/get-dishes: 
![admin_dishes](files/admin_dishes.png)  
3. Для получения всех пользователей, неодбходимо
отправить GET запрос по ${URL_ADMIN}/api/account/get-customers: 
![admin_customers](files/admin_customers.png)  
4. Для удаления блюда из меню по Id, необходимо отправить POST запрос
по ${URL_ADMIN}/api/account/delete/dish в BODY указать JSON:  
![admin_delete_dish](files/admin_delete_dish.png)  
![admin_delete_dish_1](files/admin_delete_dish_1.png)  
5. Для удаления пользователя из базы данных по Id, необходимо отправить POST запрос
по ${URL_ADMIN}/api/account/delete/customer в BODY указать JSON:  
![admin_delete_customer](files/admin_delete_customer.png)  
![admin_delete_customer_1](files/admin_delete_customer_1.png)  
6. При подсчете выручки учитываются те заказы, которые имеют статус 
COMPLETED(Заказ доставлен), подсчет выручки производится по GET запросу
${URL_ADMIN}/api/account/get-income:  
![admin_get_income](files/admin_income.png)

### Контакты  
vithag97@mail.ru
