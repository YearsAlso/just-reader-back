package com.yearsalso.data.controller;


import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yearsalso.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@Tag(name = "BaseController", description = "基础控制器")
public abstract class BaseController<E, ID extends Serializable> {

    /**
     * 获取service
     *
     * @return
     */
    public abstract IService<E> getService();


    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Operation(summary = "通过id获取")
    @Parameters({
            @Parameter(name = "id", description = "id", required = true)
    })
    public CommonResult<E> getById(@PathVariable ID id) {
        if (id == null) {
            return CommonResult.failed("Id is null");
        }
        E entity = getService().getBaseMapper().selectById(id);
        return CommonResult.success(entity);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    @Operation(summary = "获取全部数据")
    public CommonResult<List<E>> getAll() {

        List<E> list = getService().list();
        return CommonResult.success(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ResponseBody
    @Operation(summary = "分页获取")
    @Parameters({
            @Parameter(name = "page", description = "分页信息", required = true)
    })
    public CommonResult<IPage<E>> getByPage(@ModelAttribute IPage<E> page) {
        var data = getService().page(page);
        return CommonResult.success(data);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @Operation(summary = "保存数据")
    @Parameters({
            @Parameter(name = "model", description = "实体对象", required = true)
    })
    public CommonResult<Boolean> save(@ModelAttribute E model) {
        if (model == null) {
            return CommonResult.failed("entity is null");
        }

        boolean result = false;
        try {
            result = getService().save(model);
            if (!result) {
                return CommonResult.success(result);
            }
        } catch (Exception e) {
            return CommonResult.success(result, e.getMessage());
        }
        return CommonResult.success(result);
    }

    @RequestMapping(value = "/updateById", method = RequestMethod.PUT)
    @ResponseBody
    @Operation(summary = "更新数据")
    @Parameters({
            @Parameter(name = "model", description = "实体对象", required = true)
    })
    public CommonResult<Boolean> updateById(@ModelAttribute E model) {
        if (model == null) {
            return CommonResult.failed("entity is null");
        }

        boolean result = false;
        try {
            result = getService().updateById(model);
            if (!result) {
                return CommonResult.success(result);
            }
        } catch (Exception e) {
            return CommonResult.success(result, e.getMessage());
        }

        return CommonResult.success(result);
    }

    @RequestMapping(value = "/delById", method = RequestMethod.DELETE)
    @ResponseBody
    @Operation(summary = "通过id删除")
    @Parameters({
            @Parameter(name = "id", description = "id", required = true)
    })
    public CommonResult<Boolean> delById(@ModelAttribute ID id) {
        if (id == null) {
            return CommonResult.failed("id is null");
        }

        boolean result = false;
        try {
            result = getService().removeById(id);
            if (!result) {
                return CommonResult.success(result);
            }
        } catch (Exception e) {
            return CommonResult.success(result, e.getMessage());
        }

        return CommonResult.success(result);
    }


    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    @Operation(summary = "批量通过id删除")
    @Parameters({
            @Parameter(name = "ids", description = "id数组", required = true)
    })
    public CommonResult<Boolean> delAllByIds(@PathVariable ID[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            return CommonResult.failed("ids is null");
        }
        boolean result = false;
        try {
            result = getService().removeByIds(List.of(ids));
            if (!result) {
                return CommonResult.success(result);
            }
        } catch (Exception e) {
            return CommonResult.success(result, e.getMessage());
        }
        return CommonResult.success(result);
    }
}
