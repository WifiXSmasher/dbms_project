use dbmsproj;

-- drop table employees;
-- Drop the junction table first
DROP TABLE IF EXISTS Project_Client;

-- Drop the Client table
DROP TABLE IF EXISTS Client;

-- Drop the Investor table
DROP TABLE IF EXISTS Investor;

-- Drop the Budgets table
DROP TABLE IF EXISTS Budgets;

-- Drop the Project table
DROP TABLE IF EXISTS Project;

-- Drop the Performance table
DROP TABLE IF EXISTS Performance;

-- Drop the Employees table
DROP TABLE IF EXISTS employees;

-- Drop any other tables (for example, if you have a department table)
DROP TABLE IF EXISTS department;

-- Add additional DROP commands for any other tables created
DROP TABLE IF EXISTS Salary;  -- Example: Drop Salary table
DROP TABLE IF EXISTS Attendance;  -- Example: Drop Attendance table
DROP TABLE IF EXISTS Leaves;  -- Example: Drop Leave table
-- Continue adding drop commands for all other tables created

DROP DATABASE IF EXISTS dbmsproj;
CREATE DATABASE dbmsproj;
USE dbmsproj;




CREATE TABLE Department (
    dept_id INT PRIMARY KEY,
    dname VARCHAR(50) NOT NULL,
    est_date DATE,
    -- no_of_employees INT,
    email VARCHAR(100)
   --  mid INT
);
ALTER TABLE Department MODIFY dept_id INT AUTO_INCREMENT;


CREATE TABLE Department_Contact (
    dept_id INT,  -- Foreign key referencing the Department table
    contact_detail VARCHAR(100),  -- Contact detail (could be phone number or email)
    PRIMARY KEY (dept_id, contact_detail),  -- Composite primary key
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id)  -- Reference to Department table
);


CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    salary DECIMAL(15, 2) NOT NULL,
    dept_id INT,
    dob DATE NOT NULL,  -- Date of Birth
    username VARCHAR(50) UNIQUE NOT NULL,  -- Username
    password VARCHAR(255) NOT NULL,  -- Password (hashed)
    gender ENUM('Male', 'Female', 'Other') NOT NULL,  -- Gender
    role VARCHAR(50) NOT NULL,  -- Role
    emergency_contact VARCHAR(15),  -- Emergency contact number
    age INT,  -- Age to be calculated manually or via application logic
    position VARCHAR(50),
    bankAccountNumber VARCHAR(50),
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id)  -- Assuming the primary key of the department table is 'dept_id'
);

select * from department;
show tables;


CREATE TABLE employee_contacts (
    employee_id BIGINT,  -- Foreign key referencing the employee
    contact_number VARCHAR(15) NOT NULL,  -- The contact number
    PRIMARY KEY (employee_id, contact_number),  -- Composite primary key
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE  -- Assuming 'id' is the primary key in employees table
);






-- DELIMITER $$

-- CREATE TRIGGER update_age_before_insert
-- BEFORE INSERT ON employees
-- FOR EACH ROW
-- BEGIN
--     SET NEW.age = YEAR(CURDATE()) - YEAR(NEW.dob);
-- END$$

-- DELIMITER ;

-- -- Also create a trigger for update
-- DELIMITER $$

-- CREATE TRIGGER update_age_before_update
-- BEFORE UPDATE ON employees
-- FOR EACH ROW
-- BEGIN
--     SET NEW.age = YEAR(CURDATE()) - YEAR(NEW.dob);
-- END$$

-- DELIMITER ;


CREATE TABLE Performance (
    eval_id INT PRIMARY KEY,
    reviewee BIGINT,  -- Assuming this references the id in employees
    reviewer BIGINT,  -- Assuming this references the id in employees
    rating INT,
    feedback TEXT,
    date DATE NOT NULL,  -- Date of evaluation
    FOREIGN KEY (reviewee) REFERENCES employees(id),  -- Assuming emp_id is the primary key in Employee
    FOREIGN KEY (reviewer) REFERENCES employees(id)  -- Reviewer also referencing Employee
);


CREATE TABLE Attendance (
    attendance_id INT PRIMARY KEY,
    emp_id bigint, 
    login_time DATETIME,
    logout_time DATETIME,
    date DATE,
    status VARCHAR(20),
    FOREIGN KEY (emp_id) REFERENCES Employees(id) 
);


CREATE TABLE Salary (
    id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id BIGINT, 
    base_salary DECIMAL(10, 2) NOT NULL,
    hra DECIMAL(10, 2),
    medical_allowance DECIMAL(10, 2),
    travel_allowance DECIMAL(10, 2),
    pf_deduction DECIMAL(10, 2),
    start_date DATE,
    end_date DATE,
    FOREIGN KEY (emp_id) REFERENCES Employees(id) 
);

CREATE TABLE promotions (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    emp_id BIGINT,
    appraisal_amount VARCHAR(50),
    previous_position VARCHAR(50),
    new_salary DECIMAL(10, 2),
    date DATE,
    new_position VARCHAR(50),
    FOREIGN KEY (emp_id) REFERENCES employees(id)
);


CREATE TABLE Leaves (
    emp_id bigint, 
    leave_type VARCHAR(50),
    start_date DATE,
    end_date DATE,
    status VARCHAR(20),
    reason TEXT,
    PRIMARY KEY (emp_id, leave_type, start_date),
    FOREIGN KEY (emp_id) REFERENCES Employees(id) 
);


CREATE TABLE Project (
    proj_id INT PRIMARY KEY,
    pname VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    status VARCHAR(50),
    dept_id INT,  -- Foreign key to department table
    FOREIGN KEY (dept_id) REFERENCES department(dept_id)  -- Assuming the primary key of the department table is 'id'
);

-- First, drop the foreign key constraint (if named explicitly; you can find its name with SHOW CREATE TABLE employees;)

SHOW CREATE TABLE employees;  -- This will show the foreign key constraint name, which you can then use in the `DROP FOREIGN KEY` command.




-- ALTER TABLE employees 
-- ADD COLUMN project_id INT NULL,  -- Adding project_id as nullable
-- ADD FOREIGN KEY (project_id) REFERENCES Project(proj_id);  -- Setting up foreign key constraint





CREATE TABLE Budgets (
    budget_id INT PRIMARY KEY,  -- Unique identifier for the budget
    amount DECIMAL(15, 2) NOT NULL,  -- Budget amount
    status VARCHAR(50) NOT NULL,  -- Status of the budget
    proj_id INT,  -- Foreign key to the Project table
    FOREIGN KEY (proj_id) REFERENCES Project(proj_id)  -- Assuming the primary key of the Project table is 'proj_id'
);


CREATE TABLE Investor (
    investor_id INT PRIMARY KEY,  -- Unique identifier for the investor
    first_name VARCHAR(50),  -- First name of the investor
    middle_name VARCHAR(50),  -- Middle name of the investor
    last_name VARCHAR(50),  -- Last name of the investor
    address VARCHAR(255),  -- Address of the investor
    relationship_manager_id bigint,  -- Foreign key to the Employee table 
    FOREIGN KEY (relationship_manager_id) REFERENCES employees(id)  -- Assuming the primary key of the Employee table is 'id'
);

CREATE TABLE Investor_Contact (
    investor_id INT,  -- Foreign key referencing the Investor table
    contact VARCHAR(15),  -- Contact number of the investor
    PRIMARY KEY (investor_id, contact),  -- Composite primary key
    FOREIGN KEY (investor_id) REFERENCES Investor(investor_id)  -- Reference to the Investor table
);

CREATE TABLE Investor_Email (
    investor_id INT,  -- Foreign key referencing the Investor table
    email_address VARCHAR(100) UNIQUE,  -- Unique email address of the investor
	PRIMARY KEY (investor_id, email_address),  -- Composite primary key
    FOREIGN KEY (investor_id) REFERENCES Investor(investor_id)  -- Reference to the Investor table
);




CREATE TABLE Investment (
    investment_id INT PRIMARY KEY,
    investor_id INT,
    amount DECIMAL(10, 2),
    date_of_investment DATE,
    type VARCHAR(50),
    FOREIGN KEY (investor_id) REFERENCES Investor(investor_id) -- Assuming Investor table exists
);

drop table client;
CREATE TABLE Client (
    client_id INT PRIMARY KEY AUTO_INCREMENT,  -- Unique identifier for the client
    first_name VARCHAR(50) NOT NULL,  -- First name of the client
    middle_name VARCHAR(50),  -- Middle name of the client
    last_name VARCHAR(50) NOT NULL,  -- Last name of the client
    client_type VARCHAR(50)  -- Type of client (e.g., individual, corporate)
);
-- Create the junction table for the many-to-many relationship
CREATE TABLE Project_Client (
    project_id INT,  -- Foreign key referencing the Project table
    client_id INT,  -- Foreign key referencing the Client table
    PRIMARY KEY (project_id, client_id),  -- Composite primary key
    FOREIGN KEY (project_id) REFERENCES Project(proj_id),  -- Reference to Project table
    FOREIGN KEY (client_id) REFERENCES Client(client_id)  -- Reference to Client table
);


drop table if exists client_contacts;
drop table if exists Client_Emails;
drop table if exists Income;

CREATE TABLE Client_Contacts (
    client_id INT,  -- Foreign key to the Client table
    contact_number VARCHAR(15) NOT NULL,  -- Contact number of the client
    PRIMARY KEY (contact_number, client_id),  -- Composite primary key

    FOREIGN KEY (client_id) REFERENCES Client(client_id)  -- Reference to Client table
    
);


CREATE TABLE Client_Emails (
    client_id INT,  -- Foreign key to the Client table
    email_address VARCHAR(100) UNIQUE NOT NULL,  -- Email address of the client
    PRIMARY KEY (email_address, client_id),  -- Composite primary key
    FOREIGN KEY (client_id) REFERENCES Client(client_id)  -- Reference to Client table
);


CREATE TABLE Income (
    income_id INT PRIMARY KEY,
    amount DECIMAL(10, 2),
    date DATE,
    category VARCHAR(50),
    client_id INT,
    FOREIGN KEY (client_id) REFERENCES client(client_id) 
);

drop table transaction;
CREATE TABLE Transaction (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,  -- Unique identifier for the transaction
    transaction_date DATE,  -- Date of the transaction
    amount DECIMAL(10, 2),  -- Amount of the transaction
    sender_account VARCHAR(50),  -- Account from which the transaction is sent
    receiver_account VARCHAR(50),  -- Account to which the transaction is received
    transaction_type ENUM('Credit', 'Debit') NOT NULL,  -- Type of transaction (e.g., Credit or Debit)
    payment_method VARCHAR(50)  -- Method of payment (e.g., Cash, Credit Card, etc.)
);


CREATE TABLE Expense (
    expense_id INT PRIMARY KEY,
    amount DECIMAL(10, 2)
);
CREATE TABLE Category (
    category_id INT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL
);
CREATE TABLE Expense_Category (
    expense_id INT,
    category_id INT,
    PRIMARY KEY (expense_id, category_id),
    FOREIGN KEY (expense_id) REFERENCES Expense(expense_id),
    FOREIGN KEY (category_id) REFERENCES Category(category_id)
);



CREATE TABLE Expense_transaction ( 
    expense_id INT,  -- Foreign key to the Expense table
    transaction_id INT,  -- Foreign key to the Transaction table
    PRIMARY KEY (expense_id, transaction_id),  -- Composite primary key
    FOREIGN KEY (expense_id) REFERENCES Expense(expense_id),  -- Reference to the Expense table
    FOREIGN KEY (transaction_id) REFERENCES Transaction(transaction_id)  -- Reference to the Transaction table
);


CREATE TABLE Income_transaction ( 
    income_id INT,  -- Foreign key to the Income table
    transaction_id INT,  -- Foreign key to the Transaction table
    PRIMARY KEY (income_id, transaction_id),  -- Composite primary key
    FOREIGN KEY (income_id) REFERENCES Income(income_id),  -- Reference to the Income table
    FOREIGN KEY (transaction_id) REFERENCES Transaction(transaction_id)  -- Reference to the Transaction table
);
CREATE TABLE Investment_transaction ( 
    investment_id INT,  -- Foreign key to the Investment table
    transaction_id INT,  -- Foreign key to the Transaction table
    PRIMARY KEY (investment_id, transaction_id),  -- Composite primary key
    FOREIGN KEY (investment_id) REFERENCES Investment(investment_id),  -- Reference to the Investment table
    FOREIGN KEY (transaction_id) REFERENCES Transaction(transaction_id)  -- Reference to the Transaction table
);

show tables;


ALTER TABLE employees
DROP FOREIGN KEY employees_ibfk_2;  -- Replace fk_project_id with the actual constraint name

-- Then, drop the project_id column

ALTER TABLE employees
DROP COLUMN project_id;

ALTER TABLE Project 
ADD COLUMN lead_by BIGINT NULL,  -- Adding lead as a nullable foreign key
ADD FOREIGN KEY (lead_by) REFERENCES employees(id);  -- Setting up foreign key constraint


select * from employees;

ALTER TABLE employees
ADD COLUMN reports_to BIGINT,  -- Add the new 'reports_to' column
ADD CONSTRAINT fk_reports_to FOREIGN KEY (reports_to) REFERENCES employees(id);  -- Add foreign key constraint that references the same table



ALTER TABLE Department
ADD COLUMN hod_id BIGINT,  -- Add HOD column
ADD FOREIGN KEY (hod_id) REFERENCES employees(id);  -- Set HOD as a foreign key referencing employees



select * from department;
select * from project;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE employees;
TRUNCATE TABLE department;
truncate leaves;
SET FOREIGN_KEY_CHECKS = 1;


-- add alteast 3 employees to do below
UPDATE Employees
SET reports_to = 1
WHERE id IN (2, 3);



ALTER TABLE project   MODIFY status VARCHAR(50) DEFAULT 'Not Started';       
ALTER TABLE project ADD COLUMN deadline DATE NULL;  

CREATE TABLE employee_project (
    emp_id BIGINT,  -- Employee ID
    proj_id INT,    -- Project ID
    role_in_project VARCHAR(100),  -- Optional: Role of employee in the project
    PRIMARY KEY (emp_id, proj_id),  -- Composite primary key to ensure unique employee-project pairs
    FOREIGN KEY (emp_id) REFERENCES employees(id) ON DELETE CASCADE,  -- Cascade delete if an employee is deleted
    FOREIGN KEY (proj_id) REFERENCES project(proj_id) ON DELETE CASCADE  -- Cascade delete if a project is deleted
);

 alter table employee_project drop role_in_project;       





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


-- 27oct
ALTER TABLE Leaves
MODIFY status VARCHAR(20) DEFAULT 'pending';



-- manya 3 nov
alter table employees modify salary decimal (15,2) default 0;


-- 4 nov rashi

CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    emp_id BIGINT,  -- Employee ID from employees table
    notif TEXT NOT NULL,  -- Notification message content
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Timestamp for notification creation

    CONSTRAINT fk_emp_id
        FOREIGN KEY (emp_id) REFERENCES employees(id)
        ON DELETE CASCADE
);



-- 7 nov nandini 
ALTER TABLE employees ADD COLUMN position VARCHAR(50);

CREATE TABLE promotions (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    emp_id BIGINT,
    appraisal_amount VARCHAR(50),
    previous_position VARCHAR(50),
    new_salary DECIMAL(10, 2),
    date DATE,
    new_position VARCHAR(50),
    FOREIGN KEY (emp_id) REFERENCES employees(id)
);


CREATE TABLE Salary (
    id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id BIGINT, 
    base_salary DECIMAL(10, 2) NOT NULL,
    hra DECIMAL(10, 2),
    medical_allowance DECIMAL(10, 2),
    travel_allowance DECIMAL(10, 2),
    pf_deduction DECIMAL(10, 2),
    start_date DATE,
    end_date DATE,
    FOREIGN KEY (emp_id) REFERENCES Employees(id) 
);

show tables;

-- 7 nov rashi
SELECT CONSTRAINT_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'Leaves' AND COLUMN_NAME = 'emp_id';


ALTER TABLE Leaves
DROP FOREIGN KEY leaves_ibfk_1;

ALTER TABLE Leaves
DROP PRIMARY KEY;

ALTER TABLE Leaves
ADD COLUMN leaveid INT AUTO_INCREMENT PRIMARY KEY;


-- nannidni 7 nov
ALTER TABLE Employees 
ADD COLUMN bankAccountNumber VARCHAR(20);

-- 
drop table performance;
CREATE TABLE Performance (
    eval_id INT AUTO_INCREMENT PRIMARY KEY,
    reviewee BIGINT,  -- Assuming this references the id in employees
    reviewer BIGINT,  -- Assuming this references the id in employees
    project_id INT,   -- Foreign key reference to the project
    rating INT,
    feedback TEXT,
    date DATE NOT NULL,  -- Date of evaluation
    FOREIGN KEY (reviewee) REFERENCES employees(id),  -- Assuming emp_id is the primary key in Employee
    FOREIGN KEY (reviewer) REFERENCES employees(id),  -- Reviewer also referencing Employee
    FOREIGN KEY (project_id) REFERENCES Project(proj_id)  -- Reference to Project table
);



