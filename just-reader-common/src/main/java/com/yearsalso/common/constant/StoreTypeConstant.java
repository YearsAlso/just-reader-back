package com.yearsalso.common.constant;

import java.util.List;

/**
 * 存储类型常量
 */
public interface StoreTypeConstant {

    /**
     * 当前使用OSS
     */
    String OSS_USED = "OSS_USED";

    /**
     * 七牛OSS配置
     */
    String QINIU_OSS = "QINIU_OSS";

    /**
     * 七牛云存储区域 自动判断
     */
    Integer ZONE_AUTO = -1;

    /**
     * 七牛云存储区域 华东
     */
    Integer ZONE_ZERO = 0;

    /**
     * 七牛云存储区域 华北
     */
    Integer ZONE_ONE = 1;

    /**
     * 七牛云存储区域 华南
     */
    Integer ZONE_TWO = 2;

    /**
     * 七牛云存储区域 北美
     */
    Integer ZONE_THREE = 3;

    /**
     * 七牛云存储区域 东南亚
     */
    Integer ZONE_FOUR = 4;

    /**
     * 阿里OSS配置
     */
    String ALI_OSS = "ALI_OSS";

    /**
     * 腾讯COS配置
     */
    String TENCENT_OSS = "TENCENT_OSS";

    /**
     * 本地OSS配置
     */
    String LOCAL_OSS = "LOCAL_OSS";

    /**
     * Minio配置
     */
    String MINIO_OSS = "MINIO_OSS";

    /**
     * Azure BLOB配置
     */
    String AZURE_BLOB_OSS = "AZURE_BLOB_OSS";

    /**
     * 存储类型列表
     */
    List<String> STORE_TYPE_ENUM_LIST = List.of(new String[]{
            StoreTypeConstant.LOCAL_OSS,
            StoreTypeConstant.ALI_OSS,
            StoreTypeConstant.MINIO_OSS,
            StoreTypeConstant.AZURE_BLOB_OSS,
    });
}
