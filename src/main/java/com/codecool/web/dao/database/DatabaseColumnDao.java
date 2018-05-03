package com.codecool.web.dao.database;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.model.Column;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseColumnDao extends AbstractDao implements ColumnDao {

    DatabaseColumnDao(Connection connection) {
        super(connection);

    }

    @Override
    public Column insert(int scheduleId, String name) throws SQLException {
        String sql = "INSERT INTO columns (schedule_id, name) VALUES (?, ?);";
        boolean autoCommit = connection.getAutoCommit();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, scheduleId);
            statement.setString(2, name);
            statement.executeUpdate();
            int id = fetchGeneratedId(statement);
            return new Column(id, name);
        } catch (SQLException se) {
            connection.rollback();
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = " DELETE FROM columns WHERE id=?;";
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
    public void updateName(int id, String name) throws SQLException {
        String sql = "UPDATE columns SET name = ?, WHERE id = ?;";
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
    public Column findById(int id) throws SQLException, NotFoundException {
        String sql = "SELECT * FROM columns WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return fetchColumn(resultSet);
                }
            }
        }
        throw new NotFoundException();
    }

    @Override
    public List<Column> findColumnsByScheduleID(int scheduleID) throws SQLException {
        List<Column> allColumns = new ArrayList<>();
        String sql = "SELECT * FROM columns WHERE schedule_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, scheduleID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    allColumns.add(fetchColumn(resultSet));
                }
            }
        }
        return allColumns;
    }

    private Column fetchColumn(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        return new Column(id, name);
    }
}
