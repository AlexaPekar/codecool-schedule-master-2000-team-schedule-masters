package com.codecool.web.dao.database;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTaskDao extends AbstractDao implements TaskDao {


    DatabaseTaskDao(Connection connection) {
        super(connection);
    }

    @Override
    public Task insertNewTask(int userId, String name, String content) throws SQLException {
        String sql = "INSERT into tasks (user_id,name,content) VALUES (?,?,?);";
        boolean autoCommit = connection.getAutoCommit();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, userId);
            statement.setString(2, name);
            statement.setString(3, content);
            statement.executeUpdate();
            int id = fetchGeneratedId(statement);
            return new Task(id, name, content);
        } catch (SQLException se) {
            connection.rollback();
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void deleteTask(int id) throws SQLException {
        String sql = "DELETE from tasks WHERE id = ?;";
        boolean autoCommit = connection.getAutoCommit();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,id);
            statement.executeUpdate();
        } catch (SQLException se) {
            connection.rollback();
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void updateTaskName(int id, String name) throws SQLException {

    }

    @Override
    public void updateContent(int id, String content) throws SQLException {
    }

    @Override
    public Task findTaskById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Task> findAllTaskByUserId(int id) throws SQLException {
        List<Task> allTask = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    allTask.add(fetchTask(resultSet));
                }
            }
        }
        return allTask;
    }

    private Task fetchTask(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String content = resultSet.getString("content");
        return new Task(id, name, content);
    }
}
