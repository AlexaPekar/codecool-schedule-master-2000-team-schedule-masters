package com.codecool.web.dao.database;

import com.codecool.web.dao.TaskSlotScheduleDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseTaskSlotScheduleDao extends AbstractDao implements TaskSlotScheduleDao {

    public DatabaseTaskSlotScheduleDao(Connection connection) {
        super(connection);
    }

    @Override
    public void insertTaskToSlot(int slotId, int taskId, int scheduleId) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO tasks_slots (task_id, slot_id) VALUES (?, ?);\n" +
                "INSERT INTO tasks_schedules (task_id, schedule_id) VALUES (?, ?);\n";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, taskId);
            statement.setInt(2, slotId);
            statement.setInt(3, taskId);
            statement.setInt(4, scheduleId);
            executeInsert(statement);
            connection.commit();
        } catch (SQLException se) {
            connection.rollback();
            throw se;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }
}
