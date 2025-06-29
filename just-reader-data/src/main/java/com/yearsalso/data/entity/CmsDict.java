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
 * 内容-数据字典
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cms_dict")
@Schema(description = "内容-数据字典")
public class CmsDict extends BaseEntity implements Serializable {

    /**
     * 顺序
     */
    @Schema(description = "顺序")
    private BigDecimal sortOrder;

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
}
