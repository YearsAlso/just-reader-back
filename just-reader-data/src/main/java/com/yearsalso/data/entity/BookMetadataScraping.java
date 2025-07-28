package com.yearsalso.data.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "book_metadata_scraping")
@TableName("book_metadata_scraping")
@Schema(description = "书籍元数据爬取")
public class BookMetadataScraping extends BaseEntity implements Serializable {

    @Schema(description = "书籍ID")
    private Long bookId;

    @Schema(description = "元数据ID")
    private Long metadataId;

    @Schema(description = "元数据类型")
    private String metadataType;

    @Schema(description = "进度")
    private Integer progress;

    @Schema(description = "任务状态，等待中、进行中、已完成、失败")
    private String taskStatus;

    @Schema(description = "任务结果")
    private String taskResult;

    @Schema(description = "任务开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskStartTime;
}
