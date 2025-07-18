package com.yearsalso.data.service.impl;

import com.yearsalso.common.api.CommonSearch;
import com.yearsalso.data.dto.CommonPage;
import com.yearsalso.data.entity.FmsFile;
import com.yearsalso.data.mapper.FmsFileMapper;
import com.yearsalso.data.service.IFmsFileService;
import com.yearsalso.data.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件-文件 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Service("fmsFileService")
public class FmsFileServiceImpl extends ServiceImpl<FmsFileMapper, FmsFile> implements IFmsFileService {

    @Override
    public CommonPage<FmsFile> findByCondition(FmsFile file, CommonSearch searchVo, IPage<FmsFile> pageVo) {
        QueryWrapper<FmsFile> queryWrapper = new QueryWrapper<>();

        if (file != null) {

            if (file.getFileKey() != null) {
                queryWrapper.eq("file_key", file.getFileKey());
            }
            if (file.getLocationPath() != null) {
                queryWrapper.eq("location_path", file.getLocationPath());
            }
            if (file.getFileName() != null) {
                queryWrapper.eq("file_name", file.getFileName());
            }
            if (file.getFileSize() != null) {
                queryWrapper.eq("file_size", file.getFileSize());
            }
            if (file.getFileType() != null) {
                queryWrapper.eq("file_type", file.getFileType());
            }
            if (file.getFileSuffix() != null) {
                queryWrapper.eq("file_suffix", file.getFileSuffix());
            }
        }

        if (searchVo != null) {
            if (searchVo.getContainsIds() != null && !searchVo.getContainsIds().isEmpty()) {
                queryWrapper.in("id", searchVo.getContainsIds());
            } else if (searchVo.getExcludeIds() != null && !searchVo.getExcludeIds().isEmpty()) {
                queryWrapper.notIn("id", searchVo.getExcludeIds());
            }
        }


        IPage<FmsFile> fmsFileIPage = baseMapper.selectPage(pageVo, queryWrapper);

        return PageUtils.coverCommonPage(fmsFileIPage);


    }

    @Override
    @Cacheable(value = "fmsFile", key = "#fileKey")
    public FmsFile getByKey(String fileKey) {
        QueryWrapper<FmsFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FmsFile::getFileKey, fileKey);
        queryWrapper.lambda().ne(FmsFile::getDelFlag, 1);
        queryWrapper.lambda().orderByDesc(FmsFile::getCreateAt);
        queryWrapper.last("limit 1");

        return baseMapper.selectOne(queryWrapper);
    }

    @CacheEvict(value = "fmsFile", key = "#fileKey")
    public Boolean removeByKey(String fileKey) {
        QueryWrapper<FmsFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_key", fileKey);

        return baseMapper.delete(queryWrapper) > 0;
    }

    @Override
    public FmsFile findFirstByFileKey(String encode) {
        QueryWrapper<FmsFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FmsFile::getFileKey, encode);
        queryWrapper.lambda().eq(FmsFile::getDelFlag, 0);
        queryWrapper.lambda().orderByDesc(FmsFile::getCreateAt);
        queryWrapper.last("limit 1");
        return baseMapper.selectOne(queryWrapper);
    }
}
