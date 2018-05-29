package com.codecool.web.dao.database;

import com.codecool.web.dao.UserDao;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUserDao extends AbstractDao implements UserDao {

    public DatabaseUserDao(Connection connection) {
        super(connection);
    }

    @Override
    public User findByName(String name) throws SQLException, NotFoundException {
        String sql = "SELECT id, name, password, role FROM users WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return fetchUser(resultSet);
                }
            }
        }
        throw new NotFoundException();
    }

    @Override
    public User insertNewUser(String name, String password, String role) throws SQLException {
        String sql = "INSERT INTO users (name, password, role) VALUES (?, ?, ?);";
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, role);
            statement.executeUpdate();
            int id = fetchGeneratedId(statement);
            return new User(id, name, password, role);
        } catch (SQLException se) {
            connection.rollback();
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public List<User> findAllUsers() throws SQLException {
        List<User> allUsers = new ArrayList<>();
        String sql = "SELECT id, name, password, role FROM users " +
                "ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    allUsers.add(fetchUser(resultSet));
                }
            }
        }
        return allUsers;
    }

    public User findUserById(int id) throws SQLException {
        String sql = "SELECT id, name, password, role FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return fetchUser(resultSet);
                }
            }
        }
        return null;
    }

    private User fetchUser(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String password = resultSet.getString("password");
        String role = resultSet.getString("role");
        return new User(id, name, password, role);
    }
}
