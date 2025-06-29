package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "订单统计")
@TableName("das_order_statistics")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DasOrderStatistics extends BaseEntity implements Serializable {

    @Schema(description = "统计日期")
    private Date statDate;

    @Schema(description = "统计周期")
    private int statPeriod;

    @Schema(description = "统计类型, 用于区分已完成订单和已取消订单")
    private StatType statType = StatType.ALL;

    @Schema(description = "统计周期类型")
    private StatPeriodType statPeriodType = StatPeriodType.DAY;

    @Schema(description = "当前周期订单数")
    private int currentPeriodCount;

    @Schema(description = "上一周期订单数")
    private int previousPeriodCount;

    @Schema(description = "环比增长率")
    private BigDecimal samePeriodLastYearCount = BigDecimal.valueOf(0);

    @Schema(description = "同比增长率")
    private BigDecimal yoyGrowthRate = BigDecimal.valueOf(0);

    public enum StatPeriodType {
        YEAR, MONTH, WEEK, DAY
    }

    public enum StatType {
        COMPLETED, CANCELED, ALL
    }
}
