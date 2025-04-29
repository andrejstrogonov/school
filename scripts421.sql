-- Add constraints to the Student table
ALTER TABLE Student
ADD CONSTRAINT chk_age CHECK (age >= 16),
ADD CONSTRAINT uc_student_name UNIQUE (name),
ALTER COLUMN age SET DEFAULT 20;

-- Ensure student names are NOT NULL
ALTER TABLE Student
ALTER COLUMN name SET NOT NULL;

-- Add constraints to the Faculty table
ALTER TABLE Faculty
ADD CONSTRAINT uc_faculty_name_color UNIQUE (name, color);