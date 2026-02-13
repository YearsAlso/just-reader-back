package com.yearsalso.client.modules.bookInfoPage;

import com.yearsalso.client.modules.bookInfoPage.response.BookBaseInfoResponse;
import com.yearsalso.client.modules.bookInfoPage.response.BookChapterResponse;
import com.yearsalso.client.modules.bookInfoPage.response.BookKeyTermResponse;
import com.yearsalso.client.modules.bookInfoPage.response.BookKnowledgeMapResponse;
import com.yearsalso.data.dto.CommonPage;

public interface IBookInfoPageService {
    CommonPage<BookKeyTermResponse> getKeyTerms(String bookId, Integer pageNum, Integer pageSize);

    BookKnowledgeMapResponse getKnowledgeMap(String bookId);

    BookBaseInfoResponse getBaseInfo(String bookId);

    CommonPage<BookChapterResponse> getBookChapters(String bookId, Integer pageNum, Integer pageSize);
}
