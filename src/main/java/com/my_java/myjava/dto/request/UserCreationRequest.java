package com.my_java.myjava.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import com.my_java.myjava.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Tự động tạo getter & setter
@NoArgsConstructor // Đi kèm với @Data để override lại constructor
@AllArgsConstructor // Đi kèm với @Data để override lại constructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    String firstName;
    String lastName;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;
}
