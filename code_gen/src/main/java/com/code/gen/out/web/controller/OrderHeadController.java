package com.code.gen.out.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.code.gen.out.common.AjaxResult;
import com.code.gen.out.entity.dto.OrderHead;
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
@RequestMapping("/order")
public class OrderHeadController {

    @Autowired
    private OrderHeadService service;
    @Autowired
    private MapperFacade mf;

    @PostMapping("add")
    public AjaxResult insert() {
        OrderHead build = OrderHead.builder()
                .id("1281406735453528065")
                .orderNo("wqwqwqwqwqwq")
                .cashCoupon(1000)
                .otherDiscount(2000)
                .originalPrice(3000)
                .amount(5000)
                .status(1)
                .payStatus(2)
                .createId("wewewewewewe")
                .build();
        com.code.gen.out.entity.OrderHead orderHead = mf.map(build, com.code.gen.out.entity.OrderHead.class);

        boolean b = service.saveOrUpdate(orderHead);
        return AjaxResult.success(orderHead);
    }

    @DeleteMapping("delete")
    public AjaxResult del(){
        boolean b = service.removeById("1281147791195815937");
        return AjaxResult.success(b);
    }

    @GetMapping("one")
    public AjaxResult one(){
        List<com.code.gen.out.entity.OrderHead> id = service.getBaseMapper().selectList(new QueryWrapper<com.code.gen.out.entity.OrderHead>().eq("id", "1281154149525831682"));
        return AjaxResult.success(id);
    }

    @GetMapping("all")
    public AjaxResult all(){
        List<com.code.gen.out.entity.OrderHead> orderHeads1 = service.getBaseMapper().selectList(null);
        List<OrderHead> orderHeads = mf.mapAsList(orderHeads1, OrderHead.class);
        return AjaxResult.success(orderHeads);
    }

}

