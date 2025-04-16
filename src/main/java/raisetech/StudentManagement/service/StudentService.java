package raisetech.StudentManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    private StudentConverter converter;

    public StudentService(StudentRepository repository, StudentConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    // 学生のコース情報を取得
    public List<StudentsCourses> searchStudentsCourses(String studentId) {
        Student student = repository.searchStudent(studentId);
        if (student == null) {
            throw new RuntimeException("学生が見つかりません");
        }

        // studentId（int）に変換して検索
        int id = Integer.parseInt(studentId);
        return repository.searchStudentsCourses(id);
    }

    // 学生の一覧を取得
    public List<StudentDetail> searchStudentList() {
        List<Student> students = repository.search();
        List<StudentsCourses> studentsCoursesList = repository.searchStudentsCoursesList();
        return converter.convertStudentDetails(students, studentsCoursesList);
    }

    // 単一の学生詳細を取得
    public StudentDetail searchStudent(String id) {
        int studentId = Integer.parseInt(id);
        Student student = repository.searchStudent(id);
        List<StudentsCourses> studentsCourses = repository.searchStudentsCourses(studentId);

        StudentDetail studentDetail = new StudentDetail();
        studentDetail.setStudent(student);
        studentDetail.setStudentsCourses(studentsCourses);
        return studentDetail;
    }

    // 受講生を登録
    @Transactional
    public StudentDetail registerStudent(StudentDetail studentDetail) {
        Student student = studentDetail.getStudent();

        // 受講生を登録
        repository.registerStudent(student);

        // 受講生のコース情報も登録
        studentDetail.getStudentsCourses().forEach(studentCourse -> {
            initStudentsCourse(studentCourse, student);
            studentCourse.setStatus("仮申込");  // デフォルトで仮申込を設定
            repository.registerStudentCourses(studentCourse);
        });

        return studentDetail;
    }

    // コース情報の初期化
    private void initStudentsCourse(StudentsCourses studentCourse, Student student) {
        LocalDateTime now = LocalDateTime.now();

        studentCourse.setStudentsId(student.getId()); // setId → setStudentsId に変更
        studentCourse.setCourseStart(now);
        studentCourse.setCourseEnd(now.plusYears(1));
    }

    // 学生情報を更新
    @Transactional
    public void updateStudent(StudentDetail studentDetail) {
        repository.updateStudent(studentDetail.getStudent());

        for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
            repository.updateStudentCourses(studentsCourse);
        }
    }

    // コースの申込状況を更新
    @Transactional
    public void updateCourseStatus(String studentId, int courseId, String newStatus) {
        StudentsCourses studentsCourses = repository.findByStudentIdAndCourseId(studentId, courseId);
        if (studentsCourses != null) {
            studentsCourses.setStatus(newStatus);
            repository.updateStudentCourses(studentsCourses);
        } else {
            throw new IllegalArgumentException("この受講生のコースが見つかりません。");
        }
    }
}
