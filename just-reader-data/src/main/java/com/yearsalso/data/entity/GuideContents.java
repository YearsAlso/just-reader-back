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
@Table(name = "guide_contents")
@TableName("guide_contents")
@Schema(description = "导读内容")
public class GuideContents extends BaseEntity implements Serializable {

    @Schema(description = "导读ID")
    private Long guideId;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "内容类型")
    private String contentType;

    @Schema(description = "内容顺序")
    private Integer contentOrder;

    @Schema(description = "内容标题")
    private String contentTitle;

    @Schema(description = "内容描述")
    private String contentDescription;

    @Schema(description = "内容来源")
    private String contentSource;

    @Schema(description = "内容来源ID")
    private String contentSourceId;
}
