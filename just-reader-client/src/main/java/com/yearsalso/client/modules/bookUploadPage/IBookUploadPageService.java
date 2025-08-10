package com.yearsalso.client.modules.bookUploadPage;

import com.yearsalso.common.api.CommonResult;
import com.yearsalso.data.entity.File;

public interface IBookUploadPageService {
    CommonResult<File> importFromOtherLink(String link, String filePath);

    CommonResult<File> importFromCloudStorage(String link, String filePath);
}
