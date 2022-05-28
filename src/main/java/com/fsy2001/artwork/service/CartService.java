package com.fsy2001.artwork.service;

import com.fsy2001.artwork.exception.WebRequestException;
import com.fsy2001.artwork.model.Artwork;
import com.fsy2001.artwork.model.Cart;
import com.fsy2001.artwork.model.Order;
import com.fsy2001.artwork.model.User;
import com.fsy2001.artwork.repository.ArtworkRepository;
import com.fsy2001.artwork.repository.CartRepository;
import com.fsy2001.artwork.repository.OrderRepository;
import com.fsy2001.artwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public CartService(CartRepository cartRepository,
                       ArtworkRepository artworkRepository,
                       UserRepository userRepository,
                       OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }


    public void addItem(String username, Integer itemId) {
        User user = userRepository.getUserByUsername(username);
        Artwork artwork = artworkRepository.getById(itemId);
        Cart cart = new Cart(user, artwork);
        if (cartRepository.existsById(cart.getId()))
            throw new WebRequestException("item-already-added");

        cartRepository.save(cart);
    }

    public void deleteItem(String username, Integer itemId) {
        User user = userRepository.getUserByUsername(username);
        Artwork artwork = artworkRepository.getById(itemId);
        Cart cart = cartRepository.findByUserAndArtwork(user, artwork);
        if (cart == null)
            throw new WebRequestException("item-not-added");
        cartRepository.delete(cart);
    }

    public List<Artwork> showCart(String username) {
        User user = userRepository.getUserByUsername(username);
        return cartRepository
                .findByUser(user)
                .stream()
                .map(Cart::getArtwork) // 将每个cart替换为其包含的artwork
                .collect(Collectors.toList());
    }

    /* 清空购物车 */
    public void clearCart(String username) {
        User user = userRepository.getUserByUsername(username);
        int priceSum = 0;

        List<Cart> cartList = cartRepository.findByUser(user);
        List<Artwork> artworkList = cartList
                .stream()
                .map(Cart::getArtwork)
                .collect(Collectors.toList());
        List<Order> orderList = new LinkedList<>();

        if (artworkList.isEmpty())
            return;

        for (Artwork artwork : artworkList) {
            if (artwork.isSold())
                throw new WebRequestException("artwork-sold");
            orderList.add(new Order(artwork, username));
            priceSum += artwork.getPrice();
            artwork.setSold(true);
        }

        artworkRepository.saveAll(artworkList);
        orderRepository.saveAll(orderList);
        cartRepository.deleteAll(cartList);

        // 限定时间后取消订单并重新设置
        List<Integer> orderIdList = orderList
                .stream()
                .map(Order::getId)
                .collect(Collectors.toList());
        Timer timer = new Timer();
        timer.schedule(new CancelOrderTask(this, orderIdList),
                20000);
    }

    /* 用户限定时间内不付款，自动取消订单 */
    public void cancelOrder(List<Integer> orderIdList) {
        List<Artwork> newArtworkList = new ArrayList<>();
        List<Order> newOrderList = new ArrayList<>();

        for (Integer orderId : orderIdList) {
            Order order = orderRepository.getById(orderId); // 需要从数据库里找更新的订单信息
            if (order.getStatus().equals("waiting")) {
                order.setStatus("canceled");
                Artwork artwork = order.getArtwork();
                artwork.setSold(false);

                artworkRepository.save(artwork);
                orderRepository.save(order);
            }
        }
    }
}

/* 辅助类 */
class CancelOrderTask extends TimerTask {
    private final CartService cartService;
    private final List<Integer> orderIdList;

    public CancelOrderTask(CartService cartService,
                           List<Integer> orderIdList) {
        this.cartService = cartService;
        this.orderIdList = orderIdList;
    }

    @Override
    public void run() {
        cartService.cancelOrder(orderIdList);
    }
}