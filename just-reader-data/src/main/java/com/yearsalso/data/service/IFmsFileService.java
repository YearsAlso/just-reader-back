package com.yearsalso.data.service;

import com.yearsalso.common.api.CommonSearch;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yearsalso.data.dto.CommonPage;
import com.yearsalso.data.entity.FmsFile;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 文件-文件 服务类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
public interface IFmsFileService extends IService<FmsFile> {


    /**
     * 分页查询
     *
     * @param file
     * @param searchVo
     * @param pageVo
     * @return
     */
    CommonPage<FmsFile> findByCondition(
            FmsFile file,
            CommonSearch searchVo,
            IPage<FmsFile> pageVo
    );

    /**
     * 根据文件key获取文件信息
     *
     * @param fileKey
     * @return
     */
    @Schema(description = "根据文件key获取文件信息")
    FmsFile getByKey(String fileKey);

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
    FmsFile findFirstByFileKey(String encode);
}
