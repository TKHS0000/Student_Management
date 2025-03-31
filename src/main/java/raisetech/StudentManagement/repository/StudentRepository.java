package raisetech.StudentManagement.repository;

import org.apache.ibatis.annotations.*;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;

import java.util.List;

@Mapper // インターフェース自体に@Mapperを付ける
public interface StudentRepository {

    // 受講生全件検索
    @Select("SELECT * FROM students")
    List<Student> search();

    // 受講生IDで検索
    @Select("SELECT * FROM students WHERE id = #{Id}")
    Student searchStudent(String Id);

    // 受講生のコース情報を全件検索
    @Select("SELECT * FROM students_courses")
    List<StudentsCourses> searchStudentsCoursesList();

    // 受講生IDでコース情報を検索（引数は整数型のstudentId）
    @Select("SELECT * FROM students_courses WHERE students_id = #{studentId}")
    List<StudentsCourses> searchStudentsCourses(int studentId);

    // 受講生登録
    @Insert("INSERT INTO students(name, kana, nickname, email, region, age, gender, remark, isDeleted) "
            + "VALUES(#{name}, #{kana}, #{nickname}, #{email}, #{region}, #{age}, #{gender}, #{remark}, false)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void registerStudent(Student student);

    // 受講生コース登録
    @Insert("INSERT INTO students_courses(students_id, students_course, course_start, course_end) "
            + "VALUES(#{studentsId}, #{studentsCourse}, #{courseStart}, #{courseEnd})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void registerStudentCourses(StudentsCourses studentsCourses);

    // 受講生更新
    @Update("UPDATE students SET name = #{name}, kana = #{kana}, nickname = #{nickname}, "
            + "email = #{email}, region = #{region}, age = #{age}, gender = #{gender}, remark = #{remark}, isDeleted = #{isDeleted} WHERE id = #{id}")
    void updateStudent(Student student);

    // 受講生コース更新
    @Update("UPDATE students_courses SET students_course = #{studentsCourse} WHERE id = #{id}")
    void updateStudentCourses(StudentsCourses studentsCourses);

    // 仮メソッド（必要に応じて実装）
    void convertStudentDatails(); // 必要に応じて実装
}
