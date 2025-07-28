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
@Table(name = "read_count")
@TableName("read_count")
@Schema(description = "阅读统计")
public class ReadCount extends BaseEntity implements Serializable {
    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "书籍ID")
    private Long bookId;

    @Schema(description = "阅读次数")
    private Integer count;

    @Schema(description = "阅读时长（分钟）")
    private Integer duration;

    @Schema(description = "阅读进度")
    private Integer progress;

    @Schema(description = "阅读时间，根据阅读时间可以统计出阅读次数和阅读时长")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;
}
