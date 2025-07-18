package com.yearsalso.data.mapper;

import com.yearsalso.data.entity.FmsFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文件-文件 Mapper 接口
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Mapper
public interface FmsFileMapper extends BaseMapper<FmsFile> {

    FmsFile selectOneByFileKey(String fileKey);

    FmsFile selectByFilePath(String filePath);
}
