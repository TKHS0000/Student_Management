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


    public List<StudentDetail> searchStudentList() {
        List<Student> students = repository.search();
        List<StudentsCourses> studentsCoursesList = repository.searchStudentsCoursesList(); // 追加
        return converter.convertStudentDetails(students, studentsCoursesList);
    }


    public StudentDetail searchStudent(String id) {
        int studentId = Integer.parseInt(id); // String を int に変換
        Student student = repository.searchStudent(id);
        List<StudentsCourses> studentsCourses = repository.searchStudentsCourses(studentId); // int型に渡す
        StudentDetail studentDetail = new StudentDetail();
        studentDetail.setStudent(student);
        studentDetail.setStudentsCourses(studentsCourses);
        return studentDetail;
    }


    public List<StudentsCourses> searchStudentsCourseList() {
        return repository.searchStudentsCoursesList();
    }

    @Transactional
    public StudentDetail registerStudent(StudentDetail studentDetail) {
        Student student = studentDetail.getStudent();


        repository.registerStudent(student);


        studentDetail.getStudentsCourses().forEach(studentCourse -> {
            initStudentsCourse(studentCourse, student); // 修正: studentCourse を渡す
            repository.registerStudentCourses(studentCourse);
        });

        return studentDetail;
    }
    void initStudentsCourse(StudentsCourses studentCourse, Student student) {
        LocalDateTime now = LocalDateTime.now();

        studentCourse.setId(student.getId());
        studentCourse.setCourseStart(now);
        studentCourse.setCourseEnd(now.plusYears(1));
    }



    @Transactional
    public void updateStudent(StudentDetail studentDetail) {
        repository.updateStudent(studentDetail.getStudent());
        for(StudentsCourses studentsCourse:studentDetail.getStudentsCourses()){
            repository.updateStudentCourses(studentsCourse) ;
        }
    }


}

