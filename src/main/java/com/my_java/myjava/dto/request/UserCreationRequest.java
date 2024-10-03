package com.my_java.myjava.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data //Tự động tạo getter & setter
@NoArgsConstructor // Đi kèm với @Data để override lại constructor
@AllArgsConstructor // Đi kèm với @Data để override lại constructor
@Builder
@FieldDefaults( level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    String username;

    @Size(min=5, message = "Password must be least 5  letters!")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;

}
