package com.code.gen.out.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.code.gen.out.common.AjaxResult;
import com.code.gen.out.entity.FileMaterial;
import com.code.gen.out.entity.dto.OrderHead;
import com.code.gen.out.service.FileMaterialService;
import com.code.gen.out.service.OrderHeadService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2020-07-09
 */
@RestController
@RequestMapping("/file")
public class FileMaterialController {

    @Autowired
    private FileMaterialService fileMaterialService;
    @Autowired
    private MapperFacade mf;

    @PostMapping("add")
    public AjaxResult insert(String bizExt) {

        FileMaterial fileMaterial = new FileMaterial();
        fileMaterial.setBizExt(bizExt);
        boolean b = fileMaterialService.save(fileMaterial);
        return AjaxResult.success(fileMaterial);
    }

}

