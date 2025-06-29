package com.yearsalso.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 文件存储设置标准Dto,用于存储文件存储的配置信息
 */
@Data
@Accessors(chain = true)
@Schema(title = "FmsSettingDto", description = "文件存储设置标准Dto,用于存储文件存储的配置信息")
public class FmsSettingDto implements Serializable {

    @Schema(title = "服务商")
    private String serviceName;

    @Schema(title = "ak")
    private String accessKey;

    @Schema(title = "sk")
    private String secretKey;

    @Schema(title = "endpoint域名")
    private String endpoint;

    @Schema(title = "cdnEndpoint域名")
    private String cdnEndpoint;

    @Schema(title = "bucket空间")
    private String bucket;

    @Schema(title = "http")
    private String http;

    @Schema(title = "zone存储区域")
    private Integer zone;

    @Schema(title = "bucket存储区域")
    private String bucketRegion;

    @Schema(title = "本地存储路径")
    private String filePath;

    @Schema(title = "是否改变secrectKey")
    private Boolean changed;
}
