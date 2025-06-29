package com.yearsalso.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class UpdatePasswordDto {
    @NotEmpty
    @Schema(title = "用户名", required = true)
    private String username;

    @NotEmpty
    @Schema(title = "旧密码", required = true)
    private String oldPassword;

    @NotEmpty
    @Schema(title = "新密码", required = true)
    private String newPassword;
}
