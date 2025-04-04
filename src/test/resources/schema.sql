CREATE TABLE students (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(70) DEFAULT NULL,
  kana varchar(70) DEFAULT NULL,
  nickname varchar(60) DEFAULT NULL,
  email varchar(100) DEFAULT NULL,
  region varchar(100) DEFAULT NULL,
  age int DEFAULT NULL,
  gender varchar(30) DEFAULT NULL,
  remark varchar(100) DEFAULT NULL,
  isDeleted tinyint DEFAULT NULL
);


CREATE TABLE students_courses (
  id INT AUTO_INCREMENT PRIMARY KEY,
  students_id INT NOT NULL,
  students_course VARCHAR(80) NOT NULL,
  course_start TIMESTAMP NULL,
  course_end TIMESTAMP NULL
);
