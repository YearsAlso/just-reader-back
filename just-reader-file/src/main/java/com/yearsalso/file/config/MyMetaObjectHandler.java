package com.yearsalso.file.config;

import cn.dev33.satoken.stp.StpUtil;
import com.yearsalso.common.constant.AuthConstant;
import com.yearsalso.data.dto.SaTokenUserDto;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 字段填充审计
 *
 * @author
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    SaTokenUserDto getSaTokenUserDto() {
        try {
            //
            return (SaTokenUserDto) StpUtil.getSession().get(AuthConstant.STP_ADMIN_INFO);
        } catch (Exception e) {
            // 用户未登录
            log.error("匿名用户创建新数据");
            return null;
        }
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        SaTokenUserDto userDto = getSaTokenUserDto();

        this.setFieldValByName(
                "createBy",
                userDto != null ? "adminApp:" + userDto.getUsername() : "system",
                metaObject
        );
        this.setFieldValByName(
                "updateBy",
                userDto != null ? "adminApp:" + userDto.getUsername() : "system",
                metaObject
        );
        this.setFieldValByName(
                "createTime",
                new Date(),
                metaObject
        );
        this.setFieldValByName(
                "updateTime",
                new Date(),
                metaObject
        );
        this.setFieldValByName(
                "delFlag",
                0,
                metaObject
        );
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        SaTokenUserDto userDto = getSaTokenUserDto();
        this.setFieldValByName(
                "updateBy",
                userDto != null ? "adminApp:" + userDto.getUsername() : "system",
                metaObject
        );

        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}

