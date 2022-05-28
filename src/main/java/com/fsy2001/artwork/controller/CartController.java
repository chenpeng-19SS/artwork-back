package com.fsy2001.artwork.controller;

import com.fsy2001.artwork.model.Artwork;
import com.fsy2001.artwork.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /* 展示购物车内所有商品 */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public List<Artwork> getAllCartItem(Principal principal) {
        String username = principal.getName();
        return cartService.showCart(username);
    }

    /* 将商品加入购物车 */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{id}")
    public void addCartItem(@PathVariable("id") Integer itemId, Principal principal) {
        String username = principal.getName();
        cartService.addItem(username, itemId);
    }

    /* 从购物车中删除商品 */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/del/{id}")
    public void deleteCartItem(@PathVariable("id") Integer itemId, Principal principal) {
        String username = principal.getName();
        cartService.deleteItem(username, itemId);
    }

    /* 清空购物车 */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/clear")
    public void clearCart(Principal principal) {
        cartService.clearCart(principal.getName());
    }
}
