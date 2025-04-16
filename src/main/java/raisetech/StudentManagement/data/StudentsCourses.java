package raisetech.StudentManagement.data;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class StudentsCourses {
    private String id;
    private String studentsId;
    private String studentsCourse;
    private LocalDateTime courseStart;
    private LocalDateTime courseEnd;
    private String status;
}


