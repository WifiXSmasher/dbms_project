package com.example.demo.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Promotion;


@Repository
public class PromotionDAO {
    private final JdbcTemplate jdbcTemplate;

    public PromotionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void savePromotion(Promotion promotion, Employee employee) {
        String sql = "INSERT INTO promotions (emp_id, appraisal_amount, previous_position, new_position) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, 
            promotion.getEmpId(),
            promotion.getAppraisalAmount(),
            promotion.getPreviousPosition(), 
            promotion.getNewPosition());
            String sql_emp = "UPDATE employees SET position = ? WHERE emp_id = ?";
        jdbcTemplate.update(sql_emp, 
            employee.getPosition(),  // new position value to update
            employee.getId()      // emp_id to identify the employee record
        );

    }

    public List<Promotion> getPromotionsByEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM promotions WHERE emp_id = ?";
        return jdbcTemplate.query(sql, new Object[]{employeeId}, new PromotionRowMapper());
    }

    private static class PromotionRowMapper implements RowMapper<Promotion> {
        public Promotion mapRow(ResultSet rs, int rowNum) throws SQLException {
            Promotion promotion = new Promotion();
            promotion.setId(rs.getLong("id"));
            promotion.setEmpId(rs.getLong("emp_id"));
            promotion.setPreviousPosition(rs.getString("previous_position"));
            promotion.setNewPosition(rs.getString("new_position"));
            promotion.setAppraisalAmount(rs.getDouble("appraisal_amount"));
            return promotion;
        }
    }
}
