package com.yearsalso.common.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 通用搜索对象
 */
@Data
@Schema(description = "通用搜索对象")
public class CommonSearch {
    @Schema(title = "起始日期")
    public String startDate;

    @Schema(title = "结束日期")
    public String endDate;

    @Schema(title = "排除的id")
    public List<String> excludeIds;

    @Schema(title = "包含的id")
    public List<String> containsIds;

    @Schema(title = "过滤条件")
    public String filter;

    @Schema(title = "语言")
    public String locale;
}
