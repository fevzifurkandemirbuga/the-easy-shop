package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao {
    ShoppingCart getByUserId(int userId);

    // add additional method signatures here
    ShoppingCart addItem(int userId, int productId);

    void deleteItems(int userId);

    ShoppingCart updateItem(int userId, int productId, int quantity);
}
