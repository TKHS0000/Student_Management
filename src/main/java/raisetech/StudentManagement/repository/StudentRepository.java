package raisetech.StudentManagement.repository;

import org.apache.ibatis.annotations.*;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;

import java.util.List;

@Mapper
public interface StudentRepository {

    @Select("SELECT * FROM students")
    List<Student> search();

    @Select("SELECT * FROM students WHERE id = #{Id}")
    Student searchStudent(String Id);

    @Select("SELECT * FROM students_courses")
    List<StudentsCourses> searchStudentsCoursesList();

    @Select("SELECT * FROM students_courses WHERE students_id = #{studentsId}")
    List<StudentsCourses> searchStudentsCourses(String studentsId);


    @Insert("INSERT INTO students(name, kana, nickname, email, region, age, gender, remark, isDeleted) "
                    + "VALUES(#{name}, #{kana},#{nickname},#{email}, #{region}, #{age}, #{gender}, #{remark}, false)")

    @Options(useGeneratedKeys = true, keyProperty = "id")
    void registerStudent(Student student);


    @Insert("INSERT INTO students_courses(students_id, students_course, course_start, course_end)" +
            "VALUES(#{studentsId}, #{studentsCourse}, #{courseStart}, #{courseEnd})")

    @Options(useGeneratedKeys = true, keyProperty = "id")
    void  registerStudentCourses(StudentsCourses studentsCourses);

    @Update("UPDATE students SET name = #{name}, kana = #{kana}, nickname = #{nickname}, "
            + "email = #{email}, region = #{region}, age = #{age}, gender = #{gender}, remark = #{remark}, isDeleted = #{isDeleted} WHERE id = #{id}")
    void updateStudent(Student student);

    @Update("UPDATE students_courses SET students_course = #{studentsCourse} WHERE id = #{id}")
    void updateStudentCourses(StudentsCourses studentsCourses);


    void convertStudentDatails();
}