package org.yearup.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.data.*;
import org.yearup.models.*;

import java.security.Principal;

@RestController
@RequestMapping("orders")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class OrdersController {

    private final OrderLineItemDao orderLineItemDao;
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final ShoppingCartDao shoppingCartDao;
    private final ProfileDao profileDao;

    public OrdersController(OrderLineItemDao orderLineItemDao, OrderDao orderDao, UserDao userDao, ShoppingCartDao shoppingCartDao, ProfileDao profileDao) {
        this.orderLineItemDao = orderLineItemDao;
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.shoppingCartDao = shoppingCartDao;
        this.profileDao = profileDao;
    }

    @PostMapping()
    public ShoppingCart createOrder(Principal principal){
        int userId= userDao.getIdByUsername(principal.getName());
        ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);
        Profile profile = profileDao.getProfile(userId);


        Order order = orderDao.createOrder(userId,shoppingCart,profile);

        orderLineItemDao.createMultipleOrderLineItems(order.getOrderId(), shoppingCart);

        return shoppingCartDao.deleteItems(userId);
    }


}
