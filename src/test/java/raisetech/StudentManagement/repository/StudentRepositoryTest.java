package raisetech.StudentManagement.repository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository sut;

    @Test
    void 受講生の全件検索が行えること() {
        List<Student> actual = sut.search();
        assertThat(actual.size()).isEqualTo(3);
    }

    @Test
    void 受講生の登録が行われること() {
        Student student = new Student();
        student.setName("田中太郎");
        student.setKana("タナカタロウ");
        student.setNickname("タロ");
        student.setEmail("TNKA@email.com");
        student.setRegion("神奈川");
        student.setAge(30);
        student.setGender("男性");
        student.setDeleted(false); // 修正

        sut.registerStudent(student);

        List<Student> actual = sut.search();
        assertThat(actual.size()).isEqualTo(4);
    }



    @Test
    void 受講生のコース情報をIDで検索ができること() {
        List<StudentsCourses> actual = sut.searchStudentsCourses(1);
        assertThat(actual.size()).isEqualTo(1);  // studentId = 1のコースが1件のみ取得されることを期待

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        StudentsCourses firstCourse = actual.get(0);

        assertThat(firstCourse.getStudentsId()).isEqualTo("1");
        assertThat(firstCourse.getStudentsCourse()).isEqualTo("Java");
        assertThat(firstCourse.getCourseStart().format(formatter)).isEqualTo("2024-04-01 00:00:00");
        assertThat(firstCourse.getCourseEnd().format(formatter)).isEqualTo("2025-03-01 00:00:00");

    }


}
