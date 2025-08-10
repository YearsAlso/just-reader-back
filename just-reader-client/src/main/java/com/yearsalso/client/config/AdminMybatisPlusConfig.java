package com.yearsalso.client.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataChangeRecorderInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Mybatis-Plus配置
 */
@Configuration
@EnableTransactionManagement
public class AdminMybatisPlusConfig {
    @Value("${data-change-recorder.enable:true}")
    private boolean enableDataChangeRecorder;

    @Value("${data-change-recorder.enable-strict-mode:false}")
    private boolean enableStrictMode;

    @Value("${data-change-recorder.batch-update-limit:1000}")
    private int batchUpdateLimit;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DataChangeRecorderInnerInterceptor dataChangeRecorderInnerInterceptor = new DataChangeRecorderInnerInterceptor();
        // 配置安全阈值，例如限制批量更新或插入的记录数不超过 1000 条
        dataChangeRecorderInnerInterceptor.setBatchUpdateLimit(batchUpdateLimit);
        interceptor.addInnerInterceptor(dataChangeRecorderInnerInterceptor);
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 如果配置多个插件, 切记分页最后添加
        return interceptor;
    }
}
