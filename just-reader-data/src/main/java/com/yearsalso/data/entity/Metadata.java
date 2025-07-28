package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "metadata")
@TableName("metadata")
@Schema(description = "元数据，指的是书籍检索、书籍内容等相关的元信息")
public class Metadata extends BaseEntity implements Serializable {

    /**
     * 元数据名称
     */
    @Schema(description = "元数据名称")
    private String metadataName;

    /**
     * 元数据值
     */
    @Schema(description = "元数据值")
    private String metadataValue;

    /**
     * 元数据键
     */
    @Schema(description = "元数据键")
    private String metadataKey;
}
