package com.my_java.myjava.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Tự động tạo getter & setter
@NoArgsConstructor // Đi kèm với @Data để override lại constructor
@AllArgsConstructor // Đi kèm với @Data để override lại constructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionCreationRequest {
    String name;
    String description;
}
