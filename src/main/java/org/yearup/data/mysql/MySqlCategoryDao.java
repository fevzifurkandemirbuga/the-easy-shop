package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        String query = """
                SELECT category_id, name, description
                FROM categories;
                """;
        // get all categories
        try (Connection connection = super.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()
        ) {
            List<Category> categories = new ArrayList<>();
            while (resultSet.next()) {

                categories.add(mapRow(resultSet));

            }

            return categories;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Category getById(int categoryId) {
        // get category by id
        String query = """
                SELECT category_id, name, description
                FROM categories
                WHERE category_id = ?;
                """;

        try (Connection connection = super.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {

            statement.setInt(1, categoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Category create(Category category) {
        // create a new category
        String query = """
                INSERT INTO categories(
                    name,
                    description)
                VALUES(?,?);
                """;

        try (Connection connection = super.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int categoryId = generatedKeys.getInt(1);

                    // get the newly inserted category
                    return getById(categoryId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
        String query = """
                UPDATE categories
                SET name = ?,
                    description = ?
                WHERE category_id = ?
                """;

        try (Connection connection = super.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId) {
        // delete category
        String query = """
                DELETE FROM categories
                WHERE category_id = ?;
                """;

        try (Connection connection = super.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, categoryId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        return new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};
    }

}
