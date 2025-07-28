package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 内容-数据字典内容
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "dict_data")
@TableName("dict_data")
@Schema(description = "内容-数据字典内容")
public class DictData extends BaseEntity implements Serializable {

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 字典标题Id
     */
    @Schema(description = "字典标题Id")
    private Long dictId;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private BigDecimal sortOrder;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Integer status;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 值
     */
    @Schema(description = "值")
    private String value;
}
