package com.example.demo.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.example.demo.entity.Department;

public class DepartmentRowMapper implements RowMapper<Department> {
    @Override
    public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
        Department department = new Department();
        department.setDeptId(rs.getInt("dept_id"));
        department.setDname(rs.getString("dname"));
        department.setEstDate(rs.getDate("est_date"));
        department.setContactDetail(rs.getString("contact_detail"));
        department.setEmail(rs.getString("email"));
        department.setHodId(rs.getLong("hod_id")); // Map HOD ID
        return department;
    }
}
