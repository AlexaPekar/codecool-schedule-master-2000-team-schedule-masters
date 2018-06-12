package com.codecool.web.dao.database;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.exceptions.EmptyFieldException;
import com.codecool.web.model.Schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseScheduleDao extends AbstractDao implements ScheduleDao {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseScheduleDao.class);

    public DatabaseScheduleDao(Connection connection) {
        super(connection);
    }

    @Override
    public Schedule findById(int id) throws SQLException {
        logger.info("finding schedule in schedules by schedule id");
        String sql = "SELECT id,name FROM schedules WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1,id);
            logger.info("sql query prepared successfully");
            try(ResultSet resultSet = statement.executeQuery()){
                logger.info("sql query executed successfully");
                if(resultSet.next()){
                    logger.info("schedule found");
                    return fetchSchedule(resultSet);
                }
            }
        }
        logger.error("sql had not executed");
        return null;
    }

    @Override
    public Schedule insertSchedule(int userId, String name) throws SQLException {
        logger.info("inserting a schedule to schedules");
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO schedules (name,user_id) VALUES (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            statement.setString(1, name);
            statement.setInt(2, userId);
            executeInsert(statement);
            int id = fetchGeneratedId(statement);
            connection.commit();
            logger.info("sql query executed successfully");
            return new Schedule(id,name);
        } catch (SQLException ex) {
            connection.rollback();
            logger.error("sql had not executed", ex);
            throw ex;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void updateName(int id, String name) throws SQLException, EmptyFieldException {
        logger.info("updating name of schedule in schedules");
        if (name.equals("")) {
            logger.error("schedule name must not be empty");
            throw new EmptyFieldException("Title cannot be empty");
        }
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "UPDATE schedules SET name = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            executeInsert(statement);
            connection.commit();
            logger.info("sql query executed successfully");
        } catch (SQLException ex) {
            connection.rollback();
            logger.error("sql had not executed", ex);
            throw ex;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public List<Schedule> findSchedulesByUserId(int userId) throws SQLException {
        logger.info("finding schedule in schedules by user id");
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT id,user_id,name FROM schedules WHERE user_id = ? ORDER BY id";
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1,userId);
            logger.info("sql query prepared successfully");
            try(ResultSet resultSet = statement.executeQuery()){
                logger.info("sql query executed successfully");
                while(resultSet.next()){
                    schedules.add(fetchSchedule(resultSet));
                }
            }
        }
        logger.info("schedules found");
        return schedules;
    }

    @Override
    public void deleteSchedule(int id) throws SQLException {
        logger.info("deleting a schedule from schedules");
        String sql = "DELETE from schedules WHERE id = ?";
        boolean autoCommit = connection.getAutoCommit();
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
    public int getColumnNumber(int id) throws SQLException {
        logger.info("getting number of columns in the schedule");
        String sql = "SELECT COUNT(*) as amount from columns WHERE schedule_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1,id);
            logger.info("sql query prepared successfully");
            try(ResultSet resultSet = statement.executeQuery()){
                logger.info("sql query executed successfully");
                return resultSet.getInt("amount");
            }
        }
    }

    public int findScheduleIdByTaskId(int taskId) throws SQLException {
        String sql = "SELECT schedule_id FROM tasks_schedules WHERE task_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, taskId);
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    logger.info("schedule ID found");
                    return resultSet.getInt("schedule_id");
                }
            }
        }
        logger.error("sql had not executed");
        return 0;
    }

    @Override
    public List<Integer> findScheduleIdsByTaskId(int taskId) throws SQLException {
        List<Integer> scheduleIds = new ArrayList<>();
        String sql = "SELECT schedule_id FROM tasks_schedules WHERE task_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, taskId);
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    logger.info("schedule ID found");
                    scheduleIds.add(resultSet.getInt("schedule_id"));
                }
            }
        }
        logger.error("sql had not executed");
        return scheduleIds;
    }

    public Schedule fetchSchedule(ResultSet resultset) throws SQLException {
        logger.info("fetching schedule");
        int id = resultset.getInt("id");
        String name = resultset.getString("name");
        logger.info("schedule fetched successfully");
        return new Schedule(id,name);
    }

    @Override
    public Schedule insertScheduleToPublished(int id) throws SQLException {
        logger.info("inserting a schedule to publishedschedules");
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO published_schedules (schedule_id) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            statement.setInt(1, id);
            executeInsert(statement);
            connection.commit();
            logger.info("sql query executed successfully");
            return findById(id);
        } catch (SQLException ex) {
            connection.rollback();
            logger.error("sql had not executed", ex);
            throw ex;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void removeScheduleFromPublished(int id) throws SQLException {
        logger.info("Deleting a schedule from publishedschedules");
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "DELETE FROM published_schedules WHERE schedule_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            executeInsert(statement);
            connection.commit();
            logger.info("sql query executed successfully");
        } catch (SQLException ex) {
            connection.rollback();
            logger.error("sql had not executed", ex);
            throw ex;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public boolean findPublished(int id) throws SQLException {
        logger.info("Checking if schedule with ID:{} is published",id);
        String sql = "SELECT * FROM published_schedules WHERE schedule_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1,id);
            logger.info("sql query prepared successfully");
            try(ResultSet resultSet = statement.executeQuery()){
                logger.info("sql query executed successfully");
                if(resultSet.next()){
                    logger.info("Schedule is published");
                    return true;
                }
            }
        }
        logger.error("sql had not executed");
        return false;
    }
}
