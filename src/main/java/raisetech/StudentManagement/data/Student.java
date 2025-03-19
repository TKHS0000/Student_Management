package raisetech.StudentManagement.data;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Student {

    @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください。")
    private String id ;

    @NotBlank
    private String name;

    @NotBlank
    private String kana;

    @NotBlank
    private String nickname;

    @NotBlank
    private String email;

    @NotBlank
    private String region;
    private int age;

    @NotBlank
    private String gender;

    @NotBlank
    private String remark;

    @NotBlank
    private boolean isDeleted;



}
