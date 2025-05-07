-- liquibase formatted sql

-- changeset strogonov:1
-- Create index for search by student name
CREATE INDEX idx_student_name ON student (name);

-- Create composite index for search by faculty name and color
CREATE INDEX idx_faculty_name_color ON faculty (name, color);