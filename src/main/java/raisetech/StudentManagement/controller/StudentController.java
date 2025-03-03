package raisetech.StudentManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import raisetech.StudentManagement.converter.StudentConverter;
import raisetech.StudentManagement.domain.StudentDetail ;
import raisetech.StudentManagement.data.Student;
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
     * @palam studentDetail 受講生詳細
     * @return　実行結果
     */


    @Operation(summary = "受講生一覧検索", description = "受講生の一覧を検索します。")
    @PostMapping("/registerStudent")
    public ResponseEntity<String> registerStudent(@RequestBody StudentDetail studentDetail) {
        service.registerStudent(studentDetail);
        return ResponseEntity.ok("登録処理が成功しました。");
    }

    /**
     * 受講生詳細の更新を行います。　キャンセルフラグの更新もここで行います(論理削除)
     *
     * @param studentDetail 受講生詳細
     * @return　実行結果
     */

    @Operation(summary = "受講生更新",description = "受講生を更新します。")

    @PostMapping("/updateStudent")
    public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
        service.updateStudent(studentDetail);
        return ResponseEntity.ok("更新処理が成功しました。");
    }




    @GetMapping("/studentList")
    public List<StudentDetail> getStudentList() {
        return service.searchStudentList() ;
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






}
