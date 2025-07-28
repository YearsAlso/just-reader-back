package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 勋章实体类
 * <p>
 * 用于表示系统中的勋章信息，包括名称、描述、等级、图标、背景颜色、解锁条件、奖励类型和奖励值等属性。
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "medals")
@TableName("medals")
@Schema(description = "勋章")
public class Medal extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 勋章名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 勋章描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 勋章组
     * 用于将勋章进行分组管理
     */
    @Schema(description = "勋章组")
    private String medalGroup;

    /**
     * 勋章等级
     */
    @Schema(description = "最大等级")
    private Long level;

    /**
     * 勋章图标
     */
    @Schema(description = "图标")
    private String icon;

    /**
     * 背景颜色
     */
    @Schema(description = "背景颜色")
    private String bgColor;

    /**
     * 解锁条件
     */
    @Schema(description = "解锁条件")
    private String unlockCondition;

    /**
     * 奖励类型
     */
    @Schema(description = "奖励类型")
    private String rewardType;

    /**
     * 奖励值
     */
    @Schema(description = "奖励值")
    private String rewardValue;

    /**
     * 展示顺序
     */
    @Schema(description = "展示顺序")
    private String displayOrder;
}
