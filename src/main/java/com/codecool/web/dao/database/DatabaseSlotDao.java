package com.codecool.web.dao.database;

import com.codecool.web.dao.SlotDao;
import com.codecool.web.model.Slot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
        return null;
    }

    @Override
    public List<Slot> findSlotsByColumnId(int columnId) throws SQLException {
        return null;
    }
}
