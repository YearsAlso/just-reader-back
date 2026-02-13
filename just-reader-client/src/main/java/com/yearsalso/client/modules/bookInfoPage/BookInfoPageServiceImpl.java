package com.yearsalso.client.modules.bookInfoPage;

import com.yearsalso.client.modules.bookInfoPage.response.BookBaseInfoResponse;
import com.yearsalso.client.modules.bookInfoPage.response.BookChapterResponse;
import com.yearsalso.client.modules.bookInfoPage.response.BookKeyTermResponse;
import com.yearsalso.client.modules.bookInfoPage.response.BookKnowledgeMapResponse;
import com.yearsalso.data.dto.CommonPage;
import com.yearsalso.data.service.IBookCategoryService;
import com.yearsalso.data.service.IBookMarkService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bookInfoPageServiceImpl")
public class BookInfoPageServiceImpl implements IBookInfoPageService {

    @Resource
    private IBookMarkService bookMarkService;

    @Resource
    private IBookCategoryService bookCategoryService;

    @Override
    public CommonPage<BookKeyTermResponse> getKeyTerms(String bookId, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public BookKnowledgeMapResponse getKnowledgeMap(String bookId) {
        return null;
    }

    @Override
    public BookBaseInfoResponse getBaseInfo(String bookId) {
        return null;
    }

    @Override
    public CommonPage<BookChapterResponse> getBookChapters(String bookId, Integer pageNum, Integer pageSize) {
        return null;
    }
}
