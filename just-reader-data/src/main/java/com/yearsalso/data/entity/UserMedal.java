package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "user_medals")
@TableName("user_medals")
@Schema(description = "用户勋章")
public class UserMedal extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 勋章ID
     */
    @Schema(description = "勋章ID")
    private Long medalId;

    /**
     * 是否已解锁
     */
    @Schema(description = "是否已解锁")
    private Boolean isUnlocked;

    /**
     * 解锁时间
     */
    @Schema(description = "解锁时间")
    private Long unlockTime;

    /**
     * 勋章等级
     */
    @Schema(description = "勋章等级")
    private Long level;

    /**
     * 是否展示，用于在用户界面上控制勋章的显示状态
     */
    @Schema(description = "是否显示")
    private Boolean isDisplay;
}
