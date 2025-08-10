package com.yearsalso.data.service;

import com.yearsalso.common.api.CommonSearch;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yearsalso.data.dto.CommonPage;
import com.yearsalso.data.entity.File;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 文件-文件 服务类
 * </p>
 */
public interface IFileService extends IService<File> {


    /**
     * 分页查询
     *
     * @param file
     * @param searchVo
     * @param pageVo
     * @return
     */
    CommonPage<File> findByCondition(
            File file,
            CommonSearch searchVo,
            IPage<File> pageVo
    );

    /**
     * 根据文件key获取文件信息
     *
     * @param fileKey
     * @return
     */
    @Schema(description = "根据文件key获取文件信息")
    File getByKey(String fileKey);

    /**
     * 根据文件key删除文件
     *
     * @param fileKey
     * @return
     */
    @Schema(description = "根据文件key删除文件")
    Boolean removeByKey(String fileKey);

    /**
     * 根据文件key获取文件信息
     *
     * @param encode
     * @return
     */
    @Schema(description = "根据文件key获取文件信息")
    File findFirstByFileKey(String encode);
}
