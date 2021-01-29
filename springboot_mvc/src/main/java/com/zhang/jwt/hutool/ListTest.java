package com.zhang.jwt.hutool;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListTest {
    public static void main(String[] args) {

        List<String> strList = CollUtil.newArrayList();
        for (int i = 1; i <= 20; i++) {
            strList.add("你好"+i);
        }

        String collect = strList.stream().collect(Collectors.joining(",","【","】"));
        System.out.println(collect);

        Integer[] integers = NumberUtil.generateBySet(0, strList.size(), 5);
        System.out.println(Arrays.toString(integers));


    }
}
