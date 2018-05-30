package com.codecool.web.dao.database;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.exceptions.EmptyFieldException;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.model.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTaskDao extends AbstractDao implements TaskDao {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseTaskDao.class);

    public DatabaseTaskDao(Connection connection) {
        super(connection);
    }

    @Override
    public Task insertNewTask(int userId, String name, String content) throws SQLException {
        logger.info("inserting a task to tasks");
        String sql = "INSERT into tasks (user_id,name,content) VALUES (?,?,?)";
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, userId);
            statement.setString(2, name);
            statement.setString(3, content);
            statement.executeUpdate();
            int id = fetchGeneratedId(statement);
            logger.info("sql query executed successfully");
            return new Task(id, name, content);
        } catch (SQLException se) {
            connection.rollback();
            logger.error("sql had not executed", se);
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void deleteTask(int id) throws SQLException {
        logger.info("deleting a task from tasks");
        String sql = "DELETE from tasks CASCADE WHERE id = ?";
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,id);
            statement.executeUpdate();
            logger.info("sql query executed successfully");
        } catch (SQLException se) {
            connection.rollback();
            logger.error("sql had not executed", se);
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void updateTaskName(int id, String name) throws SQLException, EmptyFieldException {
        logger.info("updating name of task in tasks");
        if (name.equals("")) {
            logger.error("task name must not be empty");
            throw new EmptyFieldException("Title cannot be empty");
        }
        String sql = "UPDATE tasks SET name = ? WHERE id = ?";
        boolean autoCommit = connection.getAutoCommit();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.executeUpdate();
            logger.info("sql query executed successfully");
        } catch (SQLException se) {
            connection.rollback();
            logger.error("sql had not executed", se);
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void updateContent(int id, String content) throws SQLException, EmptyFieldException {
        logger.info("updating content of task in tasks");
        if (content.equals("")) {
            logger.error("task content must not be empty");
            throw new EmptyFieldException("Content cannot be empty");
        }
        String sql = "UPDATE tasks SET content = ? WHERE id = ?";
        boolean autoCommit = connection.getAutoCommit();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, content);
            statement.setInt(2, id);
            statement.executeUpdate();
            logger.info("sql query executed successfully");
        } catch (SQLException se) {
            connection.rollback();
            logger.error("sql had not executed", se);
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public Task findTaskById(int id) throws SQLException, NotFoundException {
        logger.info("finding task in tasks by task id");
        String sql = "SELECT id, name, content FROM tasks WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            logger.info("sql query prepared successfully");
            try (ResultSet resultSet = statement.executeQuery()) {
                logger.info("sql query executed successfully");
                if (resultSet.next()) {
                    logger.info("task found");
                    return fetchTask(resultSet);
                }
            }
        }
        logger.error("sql had not executed", new NotFoundException());
        throw new NotFoundException();
    }

    @Override
    public List<Task> findAllTaskByUserId(int id) throws SQLException {
        logger.info("finding tasks in tasks by user id");
        List<Task> allTask = new ArrayList<>();
        String sql = "SELECT id, name, content FROM tasks WHERE user_id = ? " +
                "ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            logger.info("sql query prepared successfully");
            try (ResultSet resultSet = statement.executeQuery()) {
                logger.info("sql query executed successfully");
                while (resultSet.next()) {
                    allTask.add(fetchTask(resultSet));
                }
            }
        }
        logger.info("tasks found");
        return allTask;
    }

    @Override
    public void insertTaskIdToScheduleId(int taskId, int scheduleId) throws SQLException {
        logger.info("inserting a task in a schedule");
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO tasks_schedules (task_id, schedule_id) VALUES (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, taskId);
            statement.setInt(2, scheduleId);
            executeInsert(statement);
            connection.commit();
            logger.info("sql query executed successfully");
        } catch (SQLException se) {
            connection.rollback();
            logger.error("sql had not executed", se);
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public int findTaskIdBySlotId(int slotId) throws SQLException {
        logger.info("finding task in tasks_slots by slot id");
        String sql = "SELECT task_id FROM tasks_slots WHERE slot_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, slotId);
            logger.info("sql query prepared successfully");
            try (ResultSet resultSet = statement.executeQuery()){
                logger.info("sql query executed successfully");
                if (resultSet.next()) {
                    logger.info("task found");
                    return resultSet.getInt("task_id");
                }
            }
        }
        logger.error("sql had not executed", new NotFoundException());
        return 0;
    }

    private Task fetchTask(ResultSet resultSet) throws SQLException {
        logger.info("fetching task");
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String content = resultSet.getString("content");
        logger.info("task fetched successfully");
        return new Task(id, name, content);
    }
}
