package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {


    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    public Order getById(int orderId){

        String query= """
                SELECT *
                FROM orders
                WHERE order_id = ?
                """;
        try(Connection connection =super.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ){
            statement.setInt(1,orderId);

            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return new Order(
                            orderId,
                            resultSet.getInt("user_id"),
                            resultSet.getTimestamp("date").toLocalDateTime(),
                            resultSet.getString("address"),
                            resultSet.getString("city"),
                            resultSet.getString("state"),
                            resultSet.getString("zip"),
                            resultSet.getInt("shipping_amount")
                    );
                }


            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return  null;
    }

    public Order createOrder(int userId, ShoppingCart shoppingCart, Profile profile){

        String query= """
                INSERT INTO orders(
                user_id,
                date,
                address,
                city,
                state,
                zip,
                shipping_amount)
                VALUES(?,?,?,?,?,?,?);
                """;

        try(Connection connection = super.getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ){
            statement.setInt(1,userId);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(3, profile.getAddress());
            statement.setString(4, profile.getCity());
            statement.setString(5, profile.getState());
            statement.setString(6, profile.getZip());

            statement.setBigDecimal(7,getTotal(shoppingCart));

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return getById(keys.getInt(1));
                }
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;

    }

    public BigDecimal getTotal(ShoppingCart shoppingCart){
        BigDecimal total=new BigDecimal(0);

        for (ShoppingCartItem item : shoppingCart.getItems().values()) {

            BigDecimal price = item.getProduct().getPrice();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());

            BigDecimal itemTotal = price.multiply(quantity);

            total = total.add(itemTotal);
        }

        return total;
    }

}
