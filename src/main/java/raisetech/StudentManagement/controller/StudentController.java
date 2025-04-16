package raisetech.StudentManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;  // これをインポート
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import raisetech.StudentManagement.converter.StudentConverter;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.service.StudentService;

import java.util.Arrays;
import java.util.List;

@RestController
public class StudentController {

    private StudentService service;
    private StudentConverter converter;

    @Autowired
    public StudentController(StudentService service, StudentConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    /**
     * 受講生詳細の登録を行います。
     *
     * @param studentDetail 受講生詳細
     * @return 実行結果
     */
    @Operation(summary = "受講生一覧検索", description = "受講生の一覧を検索します。")
    @PostMapping("/registerStudent")
    public ResponseEntity<String> registerStudent(@RequestBody StudentDetail studentDetail) {
        service.registerStudent(studentDetail); // ここで呼び出すメソッドが一意になる
        return ResponseEntity.ok("登録処理が成功しました。");
    }


    /**
     * 受講生詳細の更新を行います。キャンセルフラグの更新もここで行います(論理削除)
     *
     * @param studentDetail 受講生詳細
     * @return 実行結果
     */
    @Operation(summary = "受講生更新", description = "受講生を更新します。")
    @PostMapping("/updateStudent")
    public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
        service.updateStudent(studentDetail);
        return ResponseEntity.ok("更新処理が成功しました。");
    }

    @GetMapping("/studentList")
    public List<StudentDetail> getStudentList() {
        return service.searchStudentList();
    }

    @GetMapping("/student/{id}")
    public StudentDetail getStudent(@PathVariable String id) {
        return service.searchStudent(id);
    }

    @GetMapping("/newStudent")
    public String newStudent(Model model) {
        StudentDetail studentDetail = new StudentDetail();
        studentDetail.setStudentsCourses(Arrays.asList(new StudentsCourses()));
        model.addAttribute("studentDetail", studentDetail);
        return "registerStudent";
    }

    @GetMapping("/student/{id}/courses")
    public List<StudentsCourses> getCoursesWithStatus(@PathVariable String id) {
        return service.searchStudentsCourses(id);
    }

    @PutMapping("/student/{id}/course/{courseId}/status")
    public ResponseEntity<String> updateCourseStatus(@PathVariable String id,
                                                     @PathVariable int courseId,
                                                     @RequestBody String newStatus) {
        if (newStatus.equals("仮申込") || newStatus.equals("本申込") || newStatus.equals("受講中") || newStatus.equals("受講終了")) {
            service.updateCourseStatus(id, courseId, newStatus);
            return ResponseEntity.ok("コースの申込状況が正常に更新されました。");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("無効なステータスです。");
        }
    }
}
