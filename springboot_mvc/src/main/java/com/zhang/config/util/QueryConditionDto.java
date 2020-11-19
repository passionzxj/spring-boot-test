package com.zhang.config.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

public class QueryConditionDto {
    public static <T> QueryWrapper<T> parseWhereSql(String conditionJson) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(conditionJson)) {
            List<ConditionVo> conditionList = JSON.parseArray(conditionJson, ConditionVo.class);
            if (CollUtil.isNotEmpty(conditionList)) {
                for (ConditionVo conditionVo : conditionList) {
                    switch (conditionVo.getType()) {
                        case "eq":
                            queryWrapper.eq(conditionVo.getColumn(), conditionVo.getValue());
                            break;
                        case "ne":
                            queryWrapper.ne(conditionVo.getColumn(), conditionVo.getValue());
                            break;
                        case "like":
                            queryWrapper.like(conditionVo.getColumn(), conditionVo.getValue());
                            break;
                        case "leftlike":
                            queryWrapper.likeLeft(conditionVo.getColumn(), conditionVo.getValue());
                            break;
                        case "rightlike":
                            queryWrapper.likeRight(conditionVo.getColumn(), conditionVo.getValue());
                            break;
                        case "notlike":
                            queryWrapper.notLike(conditionVo.getColumn(), conditionVo.getValue());
                            break;
                        case "gt":
                            queryWrapper.gt(conditionVo.getColumn(), conditionVo.getValue());
                            break;
                        case "lt":
                            queryWrapper.lt(conditionVo.getColumn(), conditionVo.getValue());
                            break;
                        case "ge":
                            queryWrapper.ge(conditionVo.getColumn(), conditionVo.getValue());
                            break;
                        case "le":
                            queryWrapper.le(conditionVo.getColumn(), conditionVo.getValue());
                            break;
                    }
                }
            }
        }
        return queryWrapper;
    }

    public static <T> QueryWrapper<T> parseWhereSql222(List<ConditionVo> params) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CollUtil.isNotEmpty(params)) {
            for (ConditionVo conditionVo : params) {
                switch (conditionVo.getType()) {
                    case "eq":
                        queryWrapper.eq(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "ne":
                        queryWrapper.ne(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "like":
                        queryWrapper.like(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "leftlike":
                        queryWrapper.likeLeft(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "rightlike":
                        queryWrapper.likeRight(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "notlike":
                        queryWrapper.notLike(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "gt":
                        queryWrapper.gt(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "lt":
                        queryWrapper.lt(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "ge":
                        queryWrapper.ge(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "le":
                        queryWrapper.le(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                }
            }
        }
        return queryWrapper;
    }
}
