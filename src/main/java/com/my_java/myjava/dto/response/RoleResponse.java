package com.my_java.myjava.dto.response;

import java.util.Set;

import com.my_java.myjava.entity.Permission;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Tự động tạo getter & setter
@NoArgsConstructor // Đi kèm với @Data để override lại constructor
@AllArgsConstructor // Đi kèm với @Data để override lại constructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    String name;
    String description;
    Set<Permission> permissions;
}
