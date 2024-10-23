package com.my_java.myjava.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder // Tự động tạo builder dễ dàng tạo đối tượng mơi
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // Loại bỏ các field có giá trị NULL
public class ApiResponse<T> {
    @Builder.Default
    int code = 1000;

    String message;
    T data;
}
