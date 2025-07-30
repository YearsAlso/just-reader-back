package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 导读内容实体类
 * <p>
 * 用于存储和管理导读相关的内容信息，包括内容详情、类型、顺序、标题、描述以及来源等信息。
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "guide_contents")
@TableName("guide_contents")
@Schema(description = "导读内容")
public class GuideContents extends BaseEntity implements Serializable {

    /**
     * 关联的导读ID
     */
    @Schema(description = "导读ID")
    private Long guideId;

    /**
     * 内容正文
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 内容的类型（如文本、图片、视频等）
     */
    @Schema(description = "内容类型")
    private String contentType;

    /**
     * 内容在导读中的展示顺序
     */
    @Schema(description = "内容顺序")
    private Integer contentOrder;

    /**
     * 内容的标题
     */
    @Schema(description = "内容标题")
    private String contentTitle;

    /**
     * 内容的简要描述
     */
    @Schema(description = "内容描述")
    private String contentDescription;

    /**
     * 内容的来源标识（例如网站名或平台名）
     */
    @Schema(description = "内容来源")
    private String contentSource;

    /**
     * 来源系统中该内容的唯一标识符
     */
    @Schema(description = "内容来源ID")
    private String contentSourceId;
}
