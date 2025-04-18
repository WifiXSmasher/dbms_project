package com.example.demo.dao;

import java.util.List;

import com.example.demo.entity.Notification;
import com.example.demo.rowmapper.NotificationRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Notification;
import com.example.demo.rowmapper.NotificationRowMapper;

@Repository
public class NotificationDAO {

    private final JdbcTemplate jdbcTemplate;

    public NotificationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to create a new notification
    public void createNotification(Notification notification) {
        String sql = "INSERT INTO notifications (emp_id, notif) VALUES (?, ?)";
        jdbcTemplate.update(sql, notification.getEmpId(), notification.getNotif());
    }

    // Method to retrieve notifications by employee ID
    public List<Notification> getNotificationsByEmpId(Long empId) {
        String sql = "SELECT * FROM notifications WHERE emp_id = ?";
        return jdbcTemplate.query(sql, new NotificationRowMapper(), empId);
    }

    // Method to delete a notification by ID
    public void deleteNotification(Long id) {
        String sql = "DELETE FROM notifications WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Method to retrieve all notifications
    public List<Notification> getAllNotifications() {
        String sql = "SELECT * FROM notifications";
        return jdbcTemplate.query(sql, new NotificationRowMapper());
    }
}
