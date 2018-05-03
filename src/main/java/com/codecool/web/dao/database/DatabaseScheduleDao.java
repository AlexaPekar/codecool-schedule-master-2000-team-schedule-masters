package com.codecool.web.dao.database;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.model.Schedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseScheduleDao extends AbstractDao implements ScheduleDao {
    DatabaseScheduleDao(Connection connection) {
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
    public Schedule insertSchedule(int userId, String name) {
        return null;
    }

    @Override
    public Schedule updateName(int id, String name) {
        return null;
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
    public void deleteSchedule(int id) {

    }

    public Schedule fetchSchedule(ResultSet resultset) throws SQLException {
        int id = resultset.getInt("id");
        String name = resultset.getString("name");
        return new Schedule(id,name);
    }
}
