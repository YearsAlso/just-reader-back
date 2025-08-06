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
@Table(name = "user_survey")
@TableName("user_survey")
@Schema(description = "用户审查")
public class UserSurvey extends BaseEntity implements Serializable {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "审查ID")
    private Long surveyId;

    @Schema(description = "完成标记")
    private Boolean completedMark;

    @Schema(description = "收藏")
    private Boolean collect;
}
