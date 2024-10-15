package com.my_java.myjava.dto.request;

import com.my_java.myjava.entity.Permission;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
@Data //Tự động tạo getter & setter
@NoArgsConstructor // Đi kèm với @Data để override lại constructor
@AllArgsConstructor // Đi kèm với @Data để override lại constructor
@Builder
@FieldDefaults( level = AccessLevel.PRIVATE)
public class RoleRequest {
    String name;
    String description;
    Set<String> permissions;
}
