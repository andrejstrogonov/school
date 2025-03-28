select * from student;
-- Получить всех студентов, возраст которых находится между 10 и 20
SELECT * FROM student WHERE age BETWEEN 10 AND 20;

-- Получить всех студентов, но отобразить только список их имен
SELECT name FROM student;

-- Получить всех студентов, у которых в имени присутствует буква О
SELECT * FROM student WHERE name LIKE '%О%';

-- Получить всех студентов, у которых возраст меньше идентификатора
SELECT * FROM student WHERE age < id;

-- Получить всех студентов упорядоченных по возрасту
SELECT * FROM student ORDER BY age;