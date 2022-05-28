package com.fsy2001.artwork.service;

import com.fsy2001.artwork.exception.WebRequestException;
import com.fsy2001.artwork.model.Order;
import com.fsy2001.artwork.model.User;
import com.fsy2001.artwork.repository.OrderRepository;
import com.fsy2001.artwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public List<Order> getOrderList(String username) {
        return orderRepository.getOrderByUsername(username);
    }

    public void payOrder(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new WebRequestException("order-not-found"));

        if (!order.getStatus().equals("waiting"))
            throw new WebRequestException("order-canceled");

        User user = userRepository.findById(order.getUsername())
                .orElseThrow(() -> new WebRequestException("user-not-found"));
        order.setStatus("paid");
        user.addBalance(-order.getArtwork().getPrice());

        if (user.getBalance() < 0)
            throw new WebRequestException("not-enough-balance");

        order.setDeliveryTime(new Date()); // 存储发货时间
        orderRepository.save(order);
        userRepository.save(user);

        // 定时更改订单运输状态
        Timer timer = new Timer();
        for (int i = 1; i <= 4; i++) {
            timer.schedule(new ShippingStatusSet(this, order, i), i * 10000);
        }
    }

    public void confirmOrder(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new WebRequestException("order-not-found"));

        if (!order.getStatus().equals("paid"))
            throw new WebRequestException("order-canceled");

        order.setStatus("received");
        orderRepository.save(order);
    }

    /* 设置运输状态 */
    public void setShippingStatus(Order order, Integer status) {
        order.setShippingStatus(status);
        orderRepository.save(order);
    }
}

class ShippingStatusSet extends TimerTask {
    private final OrderService orderService;
    private final Order order;
    private final Integer status;

    public ShippingStatusSet(OrderService orderService,
                             Order order, Integer status) {
        this.orderService = orderService;
        this.order = order;
        this.status = status;
    }

    @Override
    public void run() {
        orderService.setShippingStatus(order, status);
    }
}
