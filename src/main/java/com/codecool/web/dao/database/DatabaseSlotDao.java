package com.codecool.web.dao.database;

import com.codecool.web.dao.SlotDao;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.model.Slot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseSlotDao extends AbstractDao implements SlotDao {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSlotDao.class);

    public DatabaseSlotDao(Connection connection) {
        super(connection);
    }

    @Override
    public Slot insertNewSlot(int columnId, String timeRange) throws SQLException {
        logger.info("inserting a slot to slots");
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO slots (column_id, time_range) VALUES (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            statement.setInt(1, columnId);
            statement.setString(2, timeRange);
            executeInsert(statement);
            int id = fetchGeneratedId(statement);
            connection.commit();
            logger.info("sql query executed successfully");
            return new Slot(id,timeRange);
        } catch (SQLException ex) {
            connection.rollback();
            logger.error("sql had not executed", ex);
            throw ex;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void deleteSlot(int id) throws SQLException {
        logger.info("deleting a slot from slots");
        String sql = "DELETE from slots WHERE id = ?;";
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
    public Slot findSlotById(int id) throws SQLException {
        logger.info("finding slot in slots by slot id");
        String sql = "SELECT id,column_id,time_range FROM slots WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1,id);
            logger.info("sql query prepared successfully");
            try(ResultSet resultSet = statement.executeQuery()){
                logger.info("sql query executed successfully");
                if(resultSet.next()){
                    logger.info("schedule found");
                    return fetchSlot(resultSet);
                }
            }
        }
        logger.error("sql had not executed", new NotFoundException());
        return null;
    }

    @Override
    public List<Slot> findSlotsByColumnId(int columnId) throws SQLException {
        logger.info("finding slot in slots by column id");
        String sql = "SELECT id,column_id,time_range FROM slots WHERE column_id = ?";
        List<Slot> slots = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1,columnId);
            logger.info("sql query prepared successfully");
            try(ResultSet resultSet = statement.executeQuery()){
                logger.info("sql query executed successfully");
                while (resultSet.next()){
                    slots.add(fetchSlot(resultSet));
                }
            }
        }
        logger.info("slots found");
        return slots;
    }

    @Override
    public void insertSlotIdToTaskId(int slotId, int taskId) throws SQLException {
        logger.info("inserting a task in a slot");
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO tasks_slots (task_id, slot_id) VALUES (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, taskId);
            statement.setInt(2, slotId);
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
    public List<Integer> findSlotIdsByTaskId(int taskId) throws SQLException {
        logger.info("finding slot ids task id in tasks_slots");
        List<Integer> slotIds = new ArrayList<>();
        String sql = "SELECT slot_id FROM tasks_slots WHERE task_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, taskId);
            logger.info("sql query prepared successfully");
            try (ResultSet resultSet = statement.executeQuery()){
                logger.info("sql query executed successfully");
                while (resultSet.next()) {
                    slotIds.add(resultSet.getInt("slot_id"));
                }
            }
        }
        logger.info("slot ids found");
        return slotIds;
    }

    public Slot fetchSlot(ResultSet resultset) throws SQLException {
        logger.info("fetching slot");
        int id = resultset.getInt("id");
        String timeRange = resultset.getString("time_range");
        logger.info("slot fetched successfully");
        return new Slot(id,timeRange);
    }
}
