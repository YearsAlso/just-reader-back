package com.yearsalso.file.controller;

import com.yearsalso.common.api.CommonResult;
import com.yearsalso.common.api.CommonSearch;
import com.yearsalso.common.constant.StoreTypeConstant;
import com.yearsalso.common.exception.ApiException;
import com.yearsalso.data.dto.CommonPage;
import com.yearsalso.data.entity.File;
import com.yearsalso.data.service.IFmsFileService;
import com.yearsalso.file.FileManageFactory;
import com.yearsalso.file.manage.LocalFileManage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * 文件管理
 * @author
 */
@Slf4j
@Controller
@Tag(name = "文件管理管理接口", description = "FileController")
@RequestMapping("/file")
@Transactional
public class FileController {

    @Autowired
    private FileManageFactory fileManageFactory;

    @Resource(name = "fmsFileService")
    private IFmsFileService fmsFileService;

    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    @ResponseBody
    @Parameters({
            @Parameter(name = "file", description = "文件信息"),
            @Parameter(name = "searchVo", description = "搜索信息"),
            @Parameter(name = "pageVo", description = "分页信息")
    })
    public CommonResult<CommonPage<File>> getFileList(@ModelAttribute File file,
                                                      @ModelAttribute CommonSearch searchVo,
                                                      @ModelAttribute IPage<File> pageVo) {

        CommonPage<File> page = fmsFileService.findByCondition(file, searchVo, pageVo);
        return CommonResult.success(page);
    }

    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    @Operation(summary = "文件复制")
    @ResponseBody
    public CommonResult<Object> copy(@RequestParam String id,
                                     @RequestParam String key) throws Exception {

        File file = fmsFileService.getById(id);
        String toKey = "copy_" + key;
        if (file.getLocationPath() == null) {
            return CommonResult.failed("存储位置未知");
        }

        // 特殊处理本地服务器
        if (StoreTypeConstant.LOCAL_OSS.equals(file.getLocationPath())) {
            key = file.getUrlPath();
        }
        String newUrl = fileManageFactory.getFileManage().copyFile(key, toKey);

        File newFile = new File();
        newFile.setFileName(file.getFileName());
        newFile.setFileKey(toKey);
        newFile.setFileSize(file.getFileSize());
        newFile.setFileType(file.getFileType());
        newFile.setStoreType(file.getStoreType());
        newFile.setLocationPath(file.getLocationPath());
        newFile.setUrlPath(newUrl);
        fmsFileService.save(newFile);

        return CommonResult.success(null);
    }

    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    @Operation(summary = "文件重命名")
    @ResponseBody
    public CommonResult<Object> rename(@RequestParam String id,
                                       @RequestParam String key,
                                       @RequestParam String newKey,
                                       @RequestParam String newName) throws Exception {

        File file = fmsFileService.getById(id);
        if (file.getLocationPath() == null) {
            return CommonResult.failed("存储位置未知");
        }
        String newUrl = "";
        if (!key.equals(newKey)) {
            // 特殊处理本地服务器
            if (StoreTypeConstant.LOCAL_OSS.equals(file.getLocationPath())) {
                key = file.getUrlPath();
            }
            newUrl = fileManageFactory.getFileManage(file.getLocationPath()).renameFile(key, newKey);
        }
        file.setFileName(newName);
        file.setFileKey(newKey);
        if (!key.equals(newKey)) {
            file.setUrlPath(newUrl);
        }

        fmsFileService.updateById(file);
        return CommonResult.success(null);
    }

    @RequestMapping(value = "/delete/{ids}", method = RequestMethod.DELETE)
    @Operation(summary = "文件删除")
    @ResponseBody
    public CommonResult<Object> delete(@PathVariable String[] ids) {

        for (String id : ids) {
            File file = fmsFileService.getById(id);
            if (file.getLocationPath() == null) {
                return CommonResult.failed("存储位置未知");
            }
            // 特殊处理本地服务器
            String key = file.getFileKey();
            if (StoreTypeConstant.LOCAL_OSS.equals(file.getLocationPath())) {
                key = file.getFileKey();
            }
            fileManageFactory.getFileManage(file.getLocationPath()).deleteFile(key);
            fmsFileService.removeById(id);
        }
        return CommonResult.success(null);
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    @Operation(summary = "本地存储预览文件")
    @Parameters({
            @Parameter(name = "id", description = "文件ID", required = true)
    })
    public void view(@PathVariable String id, HttpServletResponse response) throws IOException {

        File file = fmsFileService.getById(id);
        if (file == null) {
            throw new ApiException("文件ID:" + id + "不存在");
        }
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getFileKey(), StandardCharsets.UTF_8));
        response.setContentLengthLong((long) file.getFileSize());
        response.setContentType(file.getFileType());
        response.addHeader("Accept-Ranges", "bytes");
        if (file.getFileSize() != null && file.getFileSize() > 0) {
            response.addHeader("Content-Range", "bytes " + 0 + "-" + (file.getFileSize() - 1) + "/" + file.getFileSize());
        }

        LocalFileManage.view(file.getUrlPath(), response);
    }
}
