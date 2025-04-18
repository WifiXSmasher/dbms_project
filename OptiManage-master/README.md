run this in db

ALTER TABLE employees
ADD COLUMN reports_to BIGINT,  -- Add the new 'reports_to' column
ADD CONSTRAINT fk_reports_to FOREIGN KEY (reports_to) REFERENCES employees(id);  -- Add foreign key constraint that references the same table


select * from employees;
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE employees;

TRUNCATE TABLE department;

truncate leaves;

SET FOREIGN_KEY_CHECKS = 1;

add department and employees as per your wish set hod as per your wish

department -- 1,2

employees-1,2,3,4

make leave request with 2,3,4


UPDATE Employees

SET reports_to = 1

WHERE id IN (2, 3);


employee at 1 would be able to see leave request for only 2 and 3

leave/request/all2 see the leave requst those under manager (rn hardcoded to 1=manager_emp in leavecontroller)

leave/request/all see all the leave request

leave/request/add add the leave request with the logged in employee (rn login not setup , so hardcoded to Employee_id=2 or 3)















-- manya   24 oct 
ALTER TABLE Client ADD COLUMN username VARCHAR(255);ALTER TABLE Client ADD COLUMN password VARCHAR(255);
ALTER TABLE Investor ADD COLUMN username VARCHAR(255);ALTER TABLE Investor ADD COLUMN password VARCHAR(255);    


CREATE TABLE Meeting (
    meeting_id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    relationship_manager_id BIGINT NOT NULL,  -- Match BIGINT with Employees.id
    meeting_date DATE NOT NULL,
    meeting_time TIME NOT NULL,
    meeting_status VARCHAR(50) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES Client(client_id),
    FOREIGN KEY (relationship_manager_id) REFERENCES Employees(id)
);



-- 26 oct rashi
alter table employees drop age;






