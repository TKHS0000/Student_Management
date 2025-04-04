INSERT INTO students (name, kana, nickname, email, region, age, gender)
VALUES
  ('榎本之雄', 'エノモトユキオ', 'エノユキ', 'OOOO@email.com', '三重', 27, '男性'),
  ('加藤健二', 'カトウケンジ', 'ケン', 'UCAA@email.com', '千葉', 42, '男性'),
  ('山田玲香', 'ヤマダレイカ', 'レイ', 'YRAA@email.com', '東京', 18, '女性');



INSERT INTO students_courses (students_id, students_course, course_start, course_end)
VALUES(1, 'Java', '2024-04-01 00:00:00', '2025-03-01 00:00:00'),
  (3, 'Music', '2024-10-01 00:00:00', '2026-04-01 00:00:00'),
  (2, 'Design', '2024-02-01 00:00:00', '2025-03-01 00:00:00');