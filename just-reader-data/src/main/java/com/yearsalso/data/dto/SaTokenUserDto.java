package com.yearsalso.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SaTokenUserDto {
    @Schema(title = "id")
    private String id;

    @Schema(title = "用户名")
    private String username;

    @Schema(title = "客户端类型")
    private String clientType;

    @Schema(title = "权限名称")
    private String roleName;

    @Schema(title = "权限列表")
    private List<String> permissionList;
}
