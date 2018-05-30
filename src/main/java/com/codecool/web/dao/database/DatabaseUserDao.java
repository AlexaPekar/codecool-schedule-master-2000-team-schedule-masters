package com.codecool.web.dao.database;

import com.codecool.web.dao.UserDao;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUserDao extends AbstractDao implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseUserDao.class);

    public DatabaseUserDao(Connection connection) {
        super(connection);
    }

    @Override
    public User findByName(String name) throws SQLException, NotFoundException {
        logger.info("finding user in users by user id");
        String sql = "SELECT id, name, password, role FROM users WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            logger.info("sql query prepared successfully");
            try (ResultSet resultSet = statement.executeQuery()) {
                logger.info("sql query executed successfully");
                if (resultSet.next()) {
                    return fetchUser(resultSet);
                }
            }
        }
        logger.error("sql had not executed");
        throw new NotFoundException();
    }

    @Override
    public User insertNewUser(String name, String password, String role) throws SQLException {
        logger.info("inserting a user in users");
        String sql = "INSERT INTO users (name, password, role) VALUES (?, ?, ?)";
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, role);
            statement.executeUpdate();
            int id = fetchGeneratedId(statement);
            logger.info("sql query executed successfully");
            return new User(id, name, password, role);
        } catch (SQLException se) {
            connection.rollback();
            logger.error("sql had not executed", se);
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public List<User> findAllUsers() throws SQLException {
        logger.info("finding all users in users");
        List<User> allUsers = new ArrayList<>();
        String sql = "SELECT id, name, password, role FROM users " +
                "ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            logger.info("sql query prepared successfully");
            try (ResultSet resultSet = statement.executeQuery()) {
                logger.info("sql query executed successfully");
                while (resultSet.next()) {
                    allUsers.add(fetchUser(resultSet));
                }
            }
        }
        logger.info("users found");
        return allUsers;
    }

    public User findUserById(int id) throws SQLException {
        logger.info("finding user in users by user id");
        String sql = "SELECT id, name, password, role FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            logger.info("sql query prepared successfully");
            try (ResultSet resultSet = statement.executeQuery()) {
                logger.info("sql query executed successfully");
                if (resultSet.next()) {
                    return fetchUser(resultSet);
                }
            }
        }
        logger.error("sql had not executed");
        return null;
    }

    private User fetchUser(ResultSet resultSet) throws SQLException {
        logger.info("fetching user");
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String password = resultSet.getString("password");
        String role = resultSet.getString("role");
        logger.info("task fetched successfully");
        return new User(id, name, password, role);
    }
}
