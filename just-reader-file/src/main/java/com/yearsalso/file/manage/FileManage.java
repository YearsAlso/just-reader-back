package com.yearsalso.file.manage;

import com.yearsalso.data.dto.FmsSettingDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


/**
 * 文件管理
 *
 * @author mengx
 * @date 2024/08/12
 */
public interface FileManage {

    /**
     * 获取配置
     *
     * @return
     */
    FmsSettingDto getFmsSetting() throws Exception;

    /**
     * 文件路径上传
     *
     * @param filePath
     * @param localPath
     * @return
     */
    String pathUpload(String filePath, String localPath);

    /**
     * 文件流上传
     *
     * @param inputStream
     * @param localPath
     * @param file
     * @return
     */
    String inputStreamUpload(InputStream inputStream, String localPath, MultipartFile file);

    /**
     * 重命名文件
     *
     * @param fromKey
     * @param toKey
     * @return
     */
    String renameFile(String fromKey, String toKey);

    /**
     * 拷贝文件
     *
     * @param fromKey
     * @param toKey
     * @return
     */
    String copyFile(String fromKey, String toKey);

    /**
     * 删除文件
     *
     * @param key
     */
    void deleteFile(String key);

    /**
     * 下载文件
     *
     * @param path
     * @param response
     * @return
     */
    void downloadFile(String path, HttpServletResponse response);


    // TODO: 获取剩余空间容量，单位GB
    Double getRemainingSpace();
}
