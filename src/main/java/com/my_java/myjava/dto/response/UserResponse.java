package com.my_java.myjava.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponse {
    String id;
    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
}
