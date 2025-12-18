package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;

public interface OrderLineItemDao {

    public void createSingleOrderLineItem(int orderId, Product product, int quantity);

    public void createMultipleOrderLineItems(int orderId, ShoppingCart shoppingCart);

}
