package com.yearsalso.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SaTokenDeviceDto {
    @Schema(title = "id")
    private String id;

    @Schema(title = "用户id")
    private String userId;

    @Schema(title = "用户名")
    private String username;

    @Schema(title = "客户端类型")
    private String clientType;

    @Schema(title = "设备号")
    private String deviceCode;

    @Schema(title = "设备初始化码")
    private String deviceInitCode;

}
