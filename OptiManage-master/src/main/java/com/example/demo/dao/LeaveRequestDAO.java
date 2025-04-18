package com.example.demo.dao;

import com.example.demo.entity.LeaveRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate; // Import LocalDate

@Repository
public class LeaveRequestDAO {
    private final JdbcTemplate jdbcTemplate;

    public LeaveRequestDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Add a leave request
    public void addLeaveRequest(LeaveRequest leaveRequest) {
        String sql = "INSERT INTO Leaves (emp_id, leave_type, start_date, end_date, reason) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, leaveRequest.getEmpId(), leaveRequest.getLeaveType(),
                leaveRequest.getStartDate(), leaveRequest.getEndDate(), leaveRequest.getReason());
    }

    // Get leave requests by employee ID
    public List<LeaveRequest> getLeaveRequestsByEmployeeId(Long empId) {
        String sql = "SELECT * FROM Leaves WHERE emp_id = ?";
        return jdbcTemplate.query(sql, new LeaveRequestRowMapper(), empId);
    }

    public int countByEmployeeIdAndLeaveType(Long employeeId, String leaveType) {
        String sql = "SELECT COUNT(*) FROM Leaves WHERE emp_id = ? AND leave_type = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, employeeId, leaveType);
    }
    
    public List<LeaveRequest> getLeaveRequestsByManagerId(Long managerId) {
        String sql = "SELECT * FROM Leaves WHERE emp_id IN (SELECT id FROM Employees WHERE reports_to = ?)";
        return jdbcTemplate.query(sql, new Object[]{managerId}, new LeaveRequestRowMapper());
    }
    
    // Get all leave requests
    public List<LeaveRequest> getAllLeaveRequests() {
        String sql = "SELECT * FROM Leaves";
        return jdbcTemplate.query(sql, new LeaveRequestRowMapper());
    }

    // Update the status of a leave request
    public void updateLeaveRequestStatus(Long empId, String leaveType, LocalDate startDate, String status) {
        String sql = "UPDATE Leaves SET status = ? WHERE emp_id = ? AND leave_type = ? AND start_date = ?";
        jdbcTemplate.update(sql, status, empId, leaveType, startDate);
    }

    // Delete a leave request
    public void deleteLeaveRequest(Long empId, String leaveType, LocalDate startDate) {
        String sql = "DELETE FROM Leaves WHERE emp_id = ? AND leave_type = ? AND start_date = ?";
        jdbcTemplate.update(sql, empId, leaveType, startDate);
    }

    // RowMapper for LeaveRequest
    private static class LeaveRequestRowMapper implements RowMapper<LeaveRequest> {
        @Override
        public LeaveRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setEmpId(rs.getLong("emp_id"));
            leaveRequest.setLeaveType(rs.getString("leave_type"));
            leaveRequest.setStartDate(rs.getDate("start_date").toLocalDate());
            leaveRequest.setEndDate(rs.getDate("end_date").toLocalDate());
            leaveRequest.setStatus(rs.getString("status"));
            leaveRequest.setReason(rs.getString("reason"));
            return leaveRequest;
        }
    }
}
