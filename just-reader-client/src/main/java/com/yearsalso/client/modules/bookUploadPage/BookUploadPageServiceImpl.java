package com.yearsalso.client.modules.bookUploadPage;

import com.yearsalso.common.api.CommonResult;
import com.yearsalso.data.entity.File;
import org.springframework.stereotype.Service;

@Service("bookUploadPageServiceImpl")
public class BookUploadPageServiceImpl implements IBookUploadPageService {
    @Override
    public CommonResult<File> importFromOtherLink(String link, String filePath) {
        return null;
    }

    @Override
    public CommonResult<File> importFromCloudStorage(String link, String filePath) {
        return null;
    }
}
