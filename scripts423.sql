-- Query to get names and ages of students along with their faculty names in Хогвартс
SELECT s.name, s.age, f.name AS faculty_name
FROM Student s
JOIN Faculty f ON s.faculty_id = f.id
WHERE f.school_name = 'Хогвартс';

-- Query to get students who have avatars
SELECT s.name, s.age
FROM Student s
WHERE s.avatar IS NOT NULL;