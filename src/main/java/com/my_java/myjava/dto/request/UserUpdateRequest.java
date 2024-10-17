package com.my_java.myjava.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    String username;

    @Size(min=5, message = "Password must be least 5  letters!")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;

    List<String> roles;
}
