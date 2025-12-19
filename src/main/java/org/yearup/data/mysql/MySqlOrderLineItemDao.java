package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderLineItemDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Component
public class MySqlOrderLineItemDao extends MySqlDaoBase implements OrderLineItemDao {
    public MySqlOrderLineItemDao(DataSource dataSource) {
        super(dataSource);
    }

    public void createSingleOrderLineItem(int orderId, Product product, int quantity) {

        String query = """
                INSERT INTO order_line_items(
                order_id,
                product_id,
                sales_price,
                quantity,
                discount)
                VALUES(?,?,?,?,?)
                """;

        try (Connection connection = super.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, orderId);
            statement.setInt(2, product.getProductId());
            statement.setBigDecimal(3, product.getPrice());
            statement.setInt(4, quantity);
            statement.setInt(5, 0);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void createMultipleOrderLineItems(int orderId, ShoppingCart shoppingCart) {

        Collection<ShoppingCartItem> items = shoppingCart.getItems().values();

        items.forEach(i -> createSingleOrderLineItem(orderId, i.getProduct(), i.getQuantity()));

    }


}
