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
@Table(name = "user_challenges")
@TableName("user_challenges")
@Schema(description = "用户-挑战")
public class UserChallenge extends BaseEntity implements Serializable {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "挑战ID")
    private Long challengeId;

    @Schema(description = "是否完成")
    private Boolean isCompleted;

    @Schema(description = "完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completedAt;

    @Schema(description = "挑战状态")
    private String status;

    @Schema(description = "挑战进度")
    private Double progress;


}
