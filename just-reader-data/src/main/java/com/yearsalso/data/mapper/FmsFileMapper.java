package com.yearsalso.data.mapper;

import com.yearsalso.data.entity.File;
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
public interface FmsFileMapper extends BaseMapper<File> {

    File selectOneByFileKey(String fileKey);

    File selectByFilePath(String filePath);
}
