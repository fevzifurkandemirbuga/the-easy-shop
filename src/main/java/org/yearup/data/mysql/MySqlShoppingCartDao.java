package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {

        String query= """
                SELECT quantity, sc.product_id, name, price, category_id,
                        description, subcategory, stock, featured, image_url
                FROM shopping_cart as sc
                LEFT JOIN products as p
                ON sc.product_id = p.product_id
                WHERE sc.user_id = ?;
                """;

        try(Connection connection=super.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ){
            statement.setInt(1,userId);

            try(ResultSet resultSet = statement.executeQuery()){
                ShoppingCart shoppingCart=new ShoppingCart();

                while(resultSet.next()){

                    ShoppingCartItem shoppingCartItem =new ShoppingCartItem();
                    shoppingCartItem.setProduct(new Product(
                            resultSet.getInt("product_id"),
                            resultSet.getString("name"),
                            resultSet.getBigDecimal("price"),
                            resultSet.getInt("category_id"),
                            resultSet.getString("description"),
                            resultSet.getString("subcategory"),
                            resultSet.getInt("stock"),
                            resultSet.getBoolean("featured"),
                            resultSet.getString("image_url")
                    ));
                    shoppingCartItem.setQuantity(resultSet.getInt("quantity"));

                    shoppingCart.add(shoppingCartItem);
                }
                return shoppingCart;
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public ShoppingCart addItem(int userId,int productId) {

        String query= """
                INSERT INTO shopping_cart(
                 user_id,
                 product_id,
                 quantity)
                VALUES(?, ?, 1);
                """;

        try(Connection connection = super.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ){
            statement.setInt(1,userId);
            statement.setInt(2,productId);

            statement.executeUpdate();

            return getByUserId(userId);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public ShoppingCart deleteItems(int userId) {

        String query= """
                DELETE FROM shopping_cart
                WHERE user_id = ?;
                """;

        try(Connection connection = super.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ){
            statement.setInt(1,userId);
            statement.executeUpdate();
            return getByUserId(userId);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }


    }

    @Override
    public ShoppingCart updateItem(int userId, int productId, int quantity) {

        String query= """
                UPDATE shopping_cart
                SET quantity = ?
                Where user_id =? AND product_id =?
                """;

        try(Connection connection = super.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ){

            statement.setInt(1,quantity);
            statement.setInt(2,userId);
            statement.setInt(3,productId);

            statement.executeUpdate();

            return getByUserId(userId);


        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
}
