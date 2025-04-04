package raisetech.StudentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import raisetech.StudentManagement.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @Mock
    private StudentConverter converter;

    private StudentService sut;

    @BeforeEach
    void before() {
        sut = new StudentService(repository, converter);
    }


    @Test
    void 受講生詳細登録_受講生とコース情報の登録ができていること() {

        // 事前準備
        Student student = new Student();
        student.setId("9");

        StudentsCourses studentsCourse = new StudentsCourses();
        studentsCourse.setStudentsId(student.getId()); // 修正: 学生IDを設定

        List<StudentsCourses> studentCourseList = Arrays.asList(studentsCourse);

        StudentDetail studentDetail = new StudentDetail();
        studentDetail.setStudent(student);
        studentDetail.setStudentsCourses(studentCourseList);

        // モックの設定（void メソッド用）
        doNothing().when(repository).registerStudent(any(Student.class));
        doNothing().when(repository).registerStudentCourses(any(StudentsCourses.class));

        // 実行
        StudentDetail actual = sut.registerStudent(studentDetail);

        // 検証
        assertNotNull(actual, "registerStudent の戻り値が null です");
        assertEquals("9", actual.getStudent().getId(), "学生IDが正しく設定されていません");
        assertEquals("9", studentsCourse.getStudentsId(), "コースの学生IDが正しく設定されていません");

        verify(repository, times(1)).registerStudent(student);  // 受講生が正しく登録されるか
        verify(repository, times(1)).registerStudentCourses(studentsCourse);  // 各コースが正しく登録されるか

        assertNotNull(studentsCourse.getCourseStart(), "コース開始日が設定されていません");
        assertNotNull(studentsCourse.getCourseEnd(), "コース終了日が設定されていません");
    }


    @Test
    void 受講生詳細更新_受講生とコースの情報が正しく更新されていること() {

        // 事前準備
        Student student = new Student();
        student.setId("5");

        StudentsCourses studentscourse = new StudentsCourses();
        List<StudentsCourses> studentCourseList = Arrays.asList(studentscourse);

        StudentDetail studentDetail = new StudentDetail();
        studentDetail.setStudent(student);
        studentDetail.setStudentsCourses(studentCourseList);

        // 実行
        sut.updateStudent(studentDetail);

        // 検証
        verify(repository, times(1)).updateStudent(student); // 受講生情報の更新がリポジトリで1回呼び出されていること
        verify(repository, times(1)).updateStudentCourses(studentscourse); // 各コース情報の更新がリポジトリで// 1回ずつ呼び出されていること
    }

    @Test
    void 受講生詳細一覧検索_リポジトリとコンバータの呼び出しができていること() {

        // 事前準備
        List<Student> studentList = new ArrayList<>();
        List<StudentsCourses> studentsCoursesList = new ArrayList<>();

        when(repository.search()).thenReturn(studentList);
        when(repository.searchStudentsCoursesList()).thenReturn(studentsCoursesList);


        // 実行
        List<StudentDetail> actual = sut.searchStudentList();


        // 検証
        verify(repository, times(1)).search();
        verify(repository, times(1)).searchStudentsCoursesList();

        verify(converter, times(1)).convertStudentDetails(studentList, studentsCoursesList);
    }


    @Test
    void 受講生詳細の登録_初期化処理が行われること() {
        // 事前準備
        String id = "999";
        Student student = new Student();
        student.setId(id);

        StudentsCourses studentsCourses = new StudentsCourses();
        studentsCourses.setCourseStart(LocalDateTime.of(2025, 3, 5, 12, 0)); // 固定値を設定
        studentsCourses.setCourseEnd(LocalDateTime.of(2026, 3, 5, 12, 0));

        StudentDetail studentDetail = new StudentDetail();
        studentDetail.setStudent(student);
        studentDetail.setStudentsCourses(Arrays.asList(studentsCourses));

        // 実行
        sut.updateStudent(studentDetail);

        // 検証
        assertEquals(id, studentsCourses.getStudentsId());
        assertEquals(12, studentsCourses.getCourseStart().getHour()); // 固定値と比較
        assertEquals(2026, studentsCourses.getCourseEnd().getYear()); // 1年後の値を確認
    }

}
