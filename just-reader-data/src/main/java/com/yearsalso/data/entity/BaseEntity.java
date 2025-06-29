package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体
 *
 * @author
 */
@Data
@Schema(title = "基础实体")
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private String id;

    /**
     * 创建者
     */
    @Schema(title = "创建者")
    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @LastModifiedBy
    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新时间")
    private Date updateTime;

    /**
     * 删除标志 默认0
     */
    @TableLogic
    @Schema(title = "删除标志 默认0")
    private Integer delFlag = 0;

    /**
     * 备注
     */
    @Schema(title = "注释")
    @TableField(value = "remarks")
    private String remarks;

}
