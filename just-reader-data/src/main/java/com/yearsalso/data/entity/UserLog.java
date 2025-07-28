package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户-日志
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "log")
@TableName("log")
@Schema(description = "用户-日志")
public class UserLog extends BaseEntity implements Serializable {

    /**
     * Ip
     */
    @Schema(description = "Ip")
    private String ip;

    /**
     * Ip 信息
     */
    @Schema(description = "Ip 信息")
    private String ipInfo;

    /**
     * 日志类型
     */
    @Schema(description = "日志类型")
    private Integer logType;

    /**
     * 名字
     */
    @Schema(description = "名字")
    private String name;

    /**
     * 请求参数
     */
    @Schema(description = "请求参数")
    private String requestParam;

    /**
     * 请求类型
     */
    @Schema(description = "请求类型")
    private String requestType;

    /**
     * 请求地址
     */
    @Schema(description = "请求地址")
    private String requestUrl;

    /**
     * 用户名称
     */
    @Schema(description = "用户名称")
    private String username;
}
