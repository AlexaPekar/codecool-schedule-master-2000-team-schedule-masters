package com.codecool.web.dao.database;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.exceptions.EmptyFieldException;
import com.codecool.web.model.Schedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseScheduleDao extends AbstractDao implements ScheduleDao {
    public DatabaseScheduleDao(Connection connection) {
        super(connection);
    }

    @Override
    public Schedule findById(int id) throws SQLException {
        String sql = "SELECT id,name FROM schedules WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1,id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return fetchSchedule(resultSet);
                }
            }

        }
        return null;
    }

    @Override
    public Schedule insertSchedule(int userId, String name) throws SQLException {

        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO schedules (name,user_id) VALUES (?,?);";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            statement.setString(1, name);
            statement.setInt(2, userId);
            executeInsert(statement);
            int id = fetchGeneratedId(statement);
            connection.commit();
            return new Schedule(id,name);
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void updateName(int id, String name) throws SQLException, EmptyFieldException {
        if (name.equals("")) {
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
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public List<Schedule> findSchedulesByUserId(int userId) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT id,user_id,name FROM schedules WHERE user_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1,userId);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    schedules.add(fetchSchedule(resultSet));
                }
            }

        }
        return schedules;
    }

    @Override
    public void deleteSchedule(int id) throws SQLException {
        String sql = "DELETE from schedules WHERE id = ?;";
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

    public Schedule fetchSchedule(ResultSet resultset) throws SQLException {
        int id = resultset.getInt("id");
        String name = resultset.getString("name");
        return new Schedule(id,name);
    }
}
