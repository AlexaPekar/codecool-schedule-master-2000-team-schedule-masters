package com.codecool.web.dao.database;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTaskDao extends AbstractDao implements TaskDao {


    public DatabaseTaskDao(Connection connection) {
        super(connection);
    }

    @Override
    public Task insertNewTask(int userId, String name, String content) throws SQLException {
        String sql = "INSERT into tasks (user_id,name,content) VALUES (?,?,?)";
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
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
        String sql = "DELETE from tasks CASCADE WHERE id = ?;";
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
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
        String sql = "UPDATE tasks SET name = ? WHERE id = ?;";
        boolean autoCommit = connection.getAutoCommit();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException se) {
            connection.rollback();
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void updateContent(int id, String content) throws SQLException {
        String sql = "UPDATE tasks SET content = ? WHERE id = ?;";
        boolean autoCommit = connection.getAutoCommit();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, content);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException se) {
            connection.rollback();
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public Task findTaskById(int id) throws SQLException, NotFoundException {
        String sql = "SELECT id, name, content FROM tasks WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return fetchTask(resultSet);
                }
            }
        }
        throw new NotFoundException();
    }

    @Override
    public List<Task> findAllTaskByUserId(int id) throws SQLException {
        List<Task> allTask = new ArrayList<>();
        String sql = "SELECT id, name, content FROM tasks WHERE user_id = ? " +
                "ORDER BY id";
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

    @Override
    public void insertTaskIdToScheduleId(int taskId, int scheduleId) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO tasks_schedules (task_id, schedule_id) VALUES (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, taskId);
            statement.setInt(2, scheduleId);
            executeInsert(statement);
            connection.commit();
        } catch (SQLException se) {
            connection.rollback();
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public int findTaskIdBySlotId(int slotId) throws SQLException {
        String sql = "SELECT task_id FROM tasks_slots WHERE slot_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, slotId);
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    return resultSet.getInt("task_id");
                }
            }
        }
        return 0;
    }

    private Task fetchTask(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String content = resultSet.getString("content");
        return new Task(id, name, content);
    }
}
