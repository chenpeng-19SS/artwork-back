package com.fsy2001.artwork.controller;

import com.fsy2001.artwork.model.Order;
import com.fsy2001.artwork.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<Order> getOrderList(Principal principal) {
        return orderService.getOrderList(principal.getName());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/pay")
    public void addFriend(@RequestParam(value = "order") Integer orderId) {
        orderService.payOrder(orderId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/confirm")
    public void confirmOrder(@RequestParam(value = "order") Integer orderId) {
        orderService.confirmOrder(orderId);
    }
}
