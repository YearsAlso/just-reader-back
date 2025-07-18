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
 * 文件-文件标签
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "fms_file_tag")
@TableName("fms_file_tag")
@Schema(description = "文件-文件标签")
public class FmsFileTag extends BaseEntity implements Serializable {

    /**
     * 文件 Id
     */
    @Schema(description = "文件 Id")
    private Long fileId;

    /**
     * 标签名字
     */
    @Schema(description = "标签名字")
    private String tagName;
}
