package com.codecool.web.dao.database;

import java.sql.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractDao {

    final Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);

    AbstractDao(Connection connection) {
        this.connection = connection;
    }

    void executeInsert(PreparedStatement statement) throws SQLException {
        logger.info("executing insert");
        int insertCount = statement.executeUpdate();
        if (insertCount != 1) {
            connection.rollback();
            logger.error("Excepted 1 row to be inserted", new SQLException("Expected 1 row to be inserted"));
            throw new SQLException("Expected 1 row to be inserted");
        }
    }

    int fetchGeneratedId(PreparedStatement statement) throws SQLException {
        logger.info("generating id");
        int id;
        try (ResultSet resultSet = statement.getGeneratedKeys()) {
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            } else {
                connection.rollback();
                logger.error("Expected 1 result", new SQLException("Expected 1 result"));
                throw new SQLException("Expected 1 result");
            }
        }
        connection.commit();
        logger.info("id generated");
        return id;
    }
}
