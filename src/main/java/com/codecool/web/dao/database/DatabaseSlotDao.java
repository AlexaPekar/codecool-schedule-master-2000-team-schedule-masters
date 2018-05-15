package com.codecool.web.dao.database;

import com.codecool.web.dao.SlotDao;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Slot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseSlotDao extends AbstractDao implements SlotDao {

    DatabaseSlotDao(Connection connection) {
        super(connection);
    }

    @Override
    public Slot insertNewSlot(int columnId, String timeRange) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO slots (column_id, time_range) VALUES (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            statement.setInt(1, columnId);
            statement.setString(2, timeRange);
            executeInsert(statement);
            int id = fetchGeneratedId(statement);
            connection.commit();
            return new Slot(id,timeRange);
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void deleteSlot(int id) throws SQLException {
        String sql = "DELETE from slots WHERE id = ?;";
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
    public Slot findSlotById(int id) throws SQLException {
        String sql = "SELECT id,column_id,time_range FROM slots WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1,id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return fetchSlot(resultSet);
                }
            }

        }
        return null;
    }

    @Override
    public List<Slot> findSlotsByColumnId(int columnId) throws SQLException {
        String sql = "SELECT id,column_id,time_range FROM slots WHERE column_id = ?";
        List<Slot> slots = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1,columnId);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    slots.add(fetchSlot(resultSet));
                }
            }

        }
        return slots;
    }

    @Override
    public void insertSlotIdToTaskId(int slotId, int taskId) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO tasks_slots (task_id, slot_id) VALUES (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, taskId);
            statement.setInt(2, slotId);
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
    public List<Integer> findSlotIdsByTaskId(int taskId) throws SQLException {
        List<Integer> slotIds = new ArrayList<>();
        String sql = "SELECT slot_id FROM tasks_slots WHERE task_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, taskId);
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    slotIds.add(resultSet.getInt("slot_id"));
                }
            }
        }
        return slotIds;
    }

    public Slot fetchSlot(ResultSet resultset) throws SQLException {
        int id = resultset.getInt("id");
        String timeRange = resultset.getString("time_range");
        return new Slot(id,timeRange);
    }
}
