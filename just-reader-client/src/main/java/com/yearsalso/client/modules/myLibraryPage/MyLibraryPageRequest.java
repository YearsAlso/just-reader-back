package com.yearsalso.client.modules.myLibraryPage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MyLibraryPageRequest {

    @Schema(description = "书籍名称")
    String bookName;

    // 0: All, 1: Reading, 2: Finished, 3: Unfinished
    @Schema(description = "阅读状态，0: 全部, 1: 正在阅读, 2: 已完成, 3: 未完成")
    int readStatus;
}
