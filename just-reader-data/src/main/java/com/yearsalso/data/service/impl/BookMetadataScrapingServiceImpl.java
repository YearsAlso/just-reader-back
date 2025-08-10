package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.BookMetadataScraping;
import com.yearsalso.data.mapper.BookMetadataScrapingMapper;
import com.yearsalso.data.service.IBookMetadataScrapingService;
import org.springframework.stereotype.Service;

/**
 * 书籍元数据爬取服务实现类
 *
 * @author 
 * @since 
 */
@Service
public class BookMetadataScrapingServiceImpl extends ServiceImpl<BookMetadataScrapingMapper, BookMetadataScraping> implements IBookMetadataScrapingService {

}