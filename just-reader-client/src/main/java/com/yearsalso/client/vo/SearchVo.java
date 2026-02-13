package com.yearsalso.client.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "查询Vo")
public class SearchVo {
    @Schema(description = "起始日期")
    private Long startTime;

    @Schema(description = "结束日期")
    private Long endTime;

    @Schema(description = "排除的id")
    private List<String> excludeIds;

    @Schema(description = "包含的id")
    private List<String> containsIds;

    @Schema(description = "过滤条件")
    private String filter;

    @Schema(description = "语言")
    private String locale;
    
}
