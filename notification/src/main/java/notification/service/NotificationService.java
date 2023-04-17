package notification.service;

import model.Order;

public interface NotificationService {

    void receiveOrderSaveNotice(Order order);

}
