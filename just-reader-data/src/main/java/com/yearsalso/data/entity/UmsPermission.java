package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 用户-权限
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ums_permission")
@Schema(description = "用户-权限")
public class UmsPermission extends BaseEntity implements Serializable {

    /**
     * 操作
     */
    @Schema(description = "操作")
    private Integer action;

    /**
     * 组件
     */
    @Schema(description = "组件")
    private String component;

    /**
     * 层级
     */
    @Schema(description = "层级")
    private Integer level;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 父级Id
     */
    @Schema(description = "父级Id")
    private String parentId;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private BigDecimal sortOrder;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Boolean enableStatus;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private String type;
    
    @Schema(description = "客户端类型")
    private String clientType;

    /**
     * 访问路径
     */
    @Schema(description = "访问路径")
    private String url;
}
