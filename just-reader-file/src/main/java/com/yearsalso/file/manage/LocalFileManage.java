package com.yearsalso.file.manage;


import com.yearsalso.common.constant.StoreTypeConstant;
import com.yearsalso.common.exception.ApiException;
import com.yearsalso.data.dto.FmsSettingDto;
import com.yearsalso.data.entity.Setting;
import com.yearsalso.data.mapper.CmsSettingMapper;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author
 */
@Slf4j
@Component
public class LocalFileManage implements FileManage {

    @Autowired
    private CmsSettingMapper settingDao;

    @SneakyThrows
    @Override
    public FmsSettingDto getFmsSetting() {
        Setting setting = settingDao.selectOneBySettingKey(StoreTypeConstant.LOCAL_OSS);
        String settingValue = setting.getSettingValue();
        if (StrUtil.isBlankOrUndefined(settingValue) || !JSONUtil.isTypeJSON(settingValue)) {
            throw new ApiException("您还未配置本地存储");
        }

        FmsSettingDto result = JSONUtil.toBean(settingValue, FmsSettingDto.class);
        return result;
    }

    @SneakyThrows
    @Override
    @Deprecated
    public String pathUpload(String filePath, String localPath) {
        throw new Exception("暂不支持");
    }

    /**
     * 实则为路径上传
     *
     * @param inputStream
     * @param localPath
     * @param file
     * @return
     */
    @Override
    public String inputStreamUpload(InputStream inputStream, String localPath, MultipartFile file) {

        FmsSettingDto os = getFmsSetting();
        String day = DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String path = os.getFilePath() + "/" + day;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f = new File(path + "/" + localPath);
        if (f.exists()) {
            throw new ApiException("文件名已存在");
        }
        try {
            file.transferTo(f);
            return path + "/" + localPath;
        } catch (IOException e) {
            log.error(e.toString());
            throw new ApiException("上传文件出错");
        }
    }

    /**
     * 注意此处需传入url
     *
     * @param url
     * @param toKey
     * @return
     */
    @Override
    public String renameFile(String url, String toKey) {

        String result = copyFile(url, toKey);
        deleteFile(url);
        return result;
    }

    /**
     * 注意此处需传入url
     *
     * @param url
     * @param toKey
     * @return
     */
    @Override
    public String copyFile(String url, String toKey) {

        File file = new File(url);
        FileInputStream i = null;
        FileOutputStream o = null;

        try {
            i = new FileInputStream(file);
            o = new FileOutputStream(new File(file.getParentFile() + "/" + toKey));

            byte[] buf = new byte[1024];
            int bytesRead;

            while ((bytesRead = i.read(buf)) > 0) {
                o.write(buf, 0, bytesRead);
            }

            i.close();
            o.close();
            return file.getParentFile() + "/" + toKey;
        } catch (IOException e) {
            log.error(e.toString());
            throw new ApiException("复制文件出错");
        }finally {
            if (i != null) {
                try {
                    i.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if (o != null) {
                try {
                    o.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 注意此处需传入url
     *
     * @param url
     */
    @Override
    public void deleteFile(String url) {

        File file = new File(url);
        if(!file.delete()){
            try {
                throw new Exception("文件删除失败！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件
     *
     * @param url
     * @param response
     */
    public static void view(String url, HttpServletResponse response) {

        File file = new File(url);
        FileInputStream in = null;
        OutputStream out = null;

        try {
            if (file.exists()) {
                in = new FileInputStream(file);
                out = response.getOutputStream();

                byte[] buf = new byte[1024];
                int bytesRead;

                while ((bytesRead = in.read(buf)) > 0) {
                    out.write(buf, 0, bytesRead);
                    out.flush();
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public void downloadFile(String path, HttpServletResponse response) {
        LocalFileManage.view(path, response);
    }

    @Override
    public Double getRemainingSpace() {
        return 0.0;
    }
}
