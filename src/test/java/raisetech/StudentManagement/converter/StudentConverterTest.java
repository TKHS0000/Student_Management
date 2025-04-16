package raisetech.StudentManagement.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat; // ✅ assertThatをインポート
import static org.junit.jupiter.api.Assertions.*;

class StudentConverterTest {

    private StudentConverter sut; // sutをクラス内で宣言

    @BeforeEach
    void before() {
        sut = new StudentConverter();   // 初期化
    }

    @Test
    void 受講生のリストと受講生コース情報のリストを渡して受講生詳細のリストが作成できること() {
        // Studentオブジェクトの作成
        Student student = createStudent();
        student.setId("1");
        student.setName("榎本之雄");
        student.setKana("エノモトユキオ");
        student.setNickname("エノユキ");
        student.setEmail("OOOO@email.com");
        student.setRegion("三重");
        student.setAge(37);
        student.setGender("男性");
        student.setDeleted(false); // 修正

        // StudentsCoursesオブジェクトの作成
        StudentsCourses studentsCourses = new StudentsCourses();
        studentsCourses.setId("1");
        studentsCourses.setStudentsId("1");
        studentsCourses.setStudentsCourse("Java");
        studentsCourses.setCourseStart(LocalDateTime.of(2024, 4, 1, 0, 0));
        studentsCourses.setCourseEnd(LocalDateTime.of(2025, 3, 1, 0, 0));

        // リストに追加
        List<Student> studentList = List.of(student);
        List<StudentsCourses> studentCourseList = List.of(studentsCourses);

        // sut の変換処理をテスト
        List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

        // 受講生の詳細が正しく変換されているか確認
        assertThat(actual.get(0).getStudent()).isEqualTo(student);
        assertThat(actual.get(0).getStudentsCourses()).isEqualTo(studentCourseList);
    }

    private static Student createStudent() {
        return new Student();
    }

    @Test
    void 受講生のリストと受講生コース情報のリストを渡した時に紐づかない受講生コース情報は除外されること() {
        // Studentオブジェクトの作成
        Student student = createStudent();
        student.setId("1");
        student.setName("榎本之雄");
        student.setKana("エノモトユキオ");
        student.setNickname("エノユキ");
        student.setEmail("OOOO@email.com");
        student.setRegion("三重");
        student.setAge(37);
        student.setGender("男性");
        student.setDeleted(false); // 修正

        // StudentsCoursesオブジェクトの作成
        StudentsCourses studentsCourses = new StudentsCourses();
        studentsCourses.setId("2"); // 異なるIDを設定して関連しないデータを作成
        studentsCourses.setStudentsId("3"); // 異なるIDを設定
        studentsCourses.setStudentsCourse("Java");
        studentsCourses.setCourseStart(LocalDateTime.of(2024, 4, 1, 0, 0));
        studentsCourses.setCourseEnd(LocalDateTime.of(2025, 3, 1, 0, 0));

        // リストに追加
        List<Student> studentList = List.of(student);
        List<StudentsCourses> studentCourseList = List.of(studentsCourses);

        // sut の変換処理をテスト
        List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

        // 受講生のコース情報が紐づかない場合、リストは空のはず
        assertThat(actual.get(0).getStudentsCourses()).isEmpty();
    }
}
