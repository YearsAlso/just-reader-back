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
@Table(name = "challenges")
@TableName("challenges")
@Schema(description = "挑战")
public class Challenge extends BaseEntity implements Serializable {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "背景颜色")
    private String bgColor;

    @Schema(description = "奖励类型")
    private String rewardType;

    @Schema(description = "奖励值")
    private String rewardValue;

    @Schema(description = "解锁条件")
    private String unlockCondition;

}
