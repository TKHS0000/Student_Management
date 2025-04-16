package raisetech.StudentManagement.controller;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import raisetech.StudentManagement.data.Student;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;  // 追加
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.mockito.Mockito.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import raisetech.StudentManagement.converter.StudentConverter;

import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @MockBean
    private StudentConverter converter;


    @ParameterizedTest
    @ValueSource(strings = {"test1","test2"})
    void 受講生詳細の一覧検索が実行できて空のリストが返ってくること(String value) throws Exception {
        System.out.println(value);

        mockMvc.perform(get("/studentList" ))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service, times(1)).searchStudentList();
    }



    @Test
    void 受講生詳細の検索が実行できて空で返ってくること() throws Exception {
        String id = "1";

        mockMvc.perform(get("/student/{id}",id ))
                .andExpect(status().isOk());

        verify(service, times(1)).searchStudentList();
    }

    @Test
    void 受講生詳細の更新が実行できて空で返ってくること() throws Exception {
        mockMvc.perform(post("/updateStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        "student": {
                            "name": "榎本之雄",
                            "Kana": "エノモトユキオ",
                            "Nick": "エノユキ",
                            "Email": "OOOO@email.com",
                            "Region": "三重",
                            "Gender": "男性",
                            "Remark": ""
                            },
                            "studentCourseList" : [
                            {
                              "courseName" : "Java"
                            }
                            ]
                        }
                        """))
                .andExpect(status().isOk());

        verify(service, times(1)).registerStudent(any());
    }


    @Test
    void 受講生詳細の登録が実行できて空で返ってくること() throws Exception {
        mockMvc.perform(post("/registerStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        "student": {
                            "name": "榎本之雄",
                            "Kana": "エノモトユキオ",
                            "Nick": "エノユキ",
                            "Email": "OOOO@email.com",
                            "Region": "三重",
                            "Gender": "男性",
                            "Remark": ""
                            },
                            "studentCourseList" : [
                            {
                              "id":"1",
                              "students_id":"1",
                              "students_course":"Java",
                              "course_start":"2024-04-01 00:00:00",
                              "course_end":"2025-03-01 00:00:00"
                        
                            }
                            ]
                        }
                        """))
                .andExpect(status().isOk());

        verify(service, times(1)).updateStudent(any());
    }

    @Test
    void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
        mockMvc.perform(post("/exception"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));
    }

    @Test
    void 受講生詳細の受講生で適正な値を入力した時に入力チェックに異常が発生しないこと() {
        Student student = new Student();
        student.setId("1");  // ✅ IDは数値の文字列
        student.setName("榎本之雄");
        student.setKana("エノモトユキオ");
        student.setNickname("エノユキ");
        student.setEmail("OOOO@email.com");
        student.setRegion("三重");
        student.setGender("男性");

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations.size()).isEqualTo(0);  // ✅ バリデーションエラーなし
    }
    @Test
    void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに異常が発生しないこと() {
        Student student = new Student();
        student.setId("テストです。");
        student.setName("榎本之雄");
        student.setKana("エノモトユキオ");
        student.setNickname("エノユキ");
        student.setEmail("OOOO@email.com");
        student.setRegion("三重");
        student.setGender("男性");

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message")
                .containsOnly("数字のみ入力するようにしてください。");

    }

    @Test
    void 受講生のコース申込状況が正常に設定されること() throws Exception {
        // ここでは仮にJSONとしてコースの申込状況を指定しています
        mockMvc.perform(post("/registerStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                    "student": {
                        "name": "榎本之雄",
                        "Kana": "エノモトユキオ",
                        "Nick": "エノユキ",
                        "Email": "OOOO@email.com",
                        "Region": "三重",
                        "Gender": "男性",
                        "Remark": ""
                        },
                    "studentCourseList" : [
                    {
                        "courseName" : "Java",
                        "status": "仮申込"
                    }
                    ]
                    }"""))
                .andExpect(status().isOk())  // ステータスコードが200であること
                .andExpect(content().json("{\"studentCourseList\":[{\"courseName\":\"Java\",\"status\":\"仮申込\"}]}"));  // JSONレスポンスの内容を確認

        // サービスのregisterStudentメソッドが1回呼ばれたことを確認
        verify(service, times(1)).registerStudent(any());
    }

}
