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
@Table(name = "book_survey")
@TableName("book_survey")
@Schema(description = "图书审查")
public class BookSurvey extends BaseEntity implements Serializable {

    @Schema(description = "审查内容")
    private String surveyContent;

    @Schema(description = "图书ID")
    private Long bookId;

    @Schema(description = "书名")
    private String bookName;

    @Schema(description = "文章章节Id")
    private Long bookChapterId;

    @Schema(description = "生成状态，0: 等待生成，1：已经生成")
    private Integer generateStatus;
}
