package com.codecool.web.dao.database;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.model.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseColumnDao extends AbstractDao implements ColumnDao {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseColumnDao.class);

    public DatabaseColumnDao(Connection connection) {
        super(connection);

    }

    @Override
    public Column insert(int scheduleId, String name) throws SQLException {
        logger.info("inserting a column to columns");
        String sql = "INSERT INTO columns (schedule_id, name) VALUES (?, ?)";
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, scheduleId);
            statement.setString(2, name);
            statement.executeUpdate();
            int id = fetchGeneratedId(statement);
            logger.info("sql query executed successfully");
            return new Column(id, name);
        } catch (SQLException se) {
            connection.rollback();
            logger.error("sql had not executed", se);
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        logger.info("deleting column from columns");
        String sql = " DELETE FROM columns WHERE id=?;";
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
    public void updateName(int id, String name) throws SQLException {
        logger.info("updating name of column in columns");
        String sql = "UPDATE columns SET name = ? WHERE id = ?;";
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
    public Column findById(int id) throws SQLException, NotFoundException {
        logger.info("finding column in columns by column id");
        String sql = "SELECT * FROM columns WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            logger.info("sql query prepared successfully");
            try (ResultSet resultSet = statement.executeQuery()) {
                logger.info("sql query executed successfully");
                if (resultSet.next()) {
                    logger.info("column found");
                    return fetchColumn(resultSet);
                }
            }
        }
        logger.error("sql had not executed", new NotFoundException());
        throw new NotFoundException();
    }

    @Override
    public List<Column> findColumnsByScheduleID(int scheduleID) throws SQLException {
        logger.info("finding columns in columns by schedule id");
        List<Column> allColumns = new ArrayList<>();
        String sql = "SELECT * FROM columns WHERE schedule_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, scheduleID);
            logger.info("sql query prepared successfully");
            try (ResultSet resultSet = statement.executeQuery()) {
                logger.info("sql query executed successfully");
                while (resultSet.next()) {
                    allColumns.add(fetchColumn(resultSet));
                }
            }
        }
        logger.info("columns found");
        return allColumns;
    }

    private Column fetchColumn(ResultSet resultSet) throws SQLException {
        logger.info("fetching column");
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        logger.info("column fetched successfully");
        return new Column(id, name);
    }
}
