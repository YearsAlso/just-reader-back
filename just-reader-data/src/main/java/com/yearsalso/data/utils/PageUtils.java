package com.yearsalso.data.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yearsalso.data.dto.CommonPage;

public class PageUtils {

    public static <T> CommonPage<T> coverCommonPage(IPage<T> page) {
        CommonPage<T> commonPage = new CommonPage<>();

        commonPage.setPageNum((int) page.getCurrent());
        commonPage.setPageSize((int) page.getSize());
        commonPage.setTotalPage((int) page.getPages());
        commonPage.setTotal(page.getTotal());
        commonPage.setList(page.getRecords());

        return commonPage;
    }
}
