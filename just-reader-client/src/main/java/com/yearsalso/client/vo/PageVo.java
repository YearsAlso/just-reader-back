package com.yearsalso.client.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "分页Vo")
public class PageVo implements Serializable {

    @Schema(description = "页号")
    private int current;

    @Schema(description = "页面大小")
    private int pageSize;

    @Schema(description = "排序字段")
    private String sort;

    @Schema(description = "排序多个字段")
    private String[] sorts;

    @Schema(description = "排序方式 asc/desc")
    private String orderBy;
}
