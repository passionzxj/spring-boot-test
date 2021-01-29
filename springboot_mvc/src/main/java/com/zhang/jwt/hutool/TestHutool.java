package com.zhang.jwt.hutool;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.*;

public class TestHutool {

    public static void main(String[] args) {
        /**
         * NumberUtil.generateBySet
         */
        List<String> StrList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            StrList.add("你好" + i);
        }
        List<String> list = getRandomList(StrList, new Random().nextInt(10));
        list.forEach(System.out::println);

        /**
         *
         * RandomUtil.randomInt 获得指定范围内的随机数
         * RandomUtil.randomBytes 随机bytes
         * RandomUtil.randomEle 随机获得列表中的元素
         * RandomUtil.randomEleSet 随机获得列表中的一定量的不重复元素，返回Set
         * RandomUtil.randomString 获得一个随机的字符串（只包含数字和字符）
         * RandomUtil.randomNumbers 获得一个只包含数字的字符串
         * RandomUtil.randomUUID 随机UUID
         * RandomUtil.weightRandom 权重随机生成器，传入带权重的对象，然后根据权重随机获取对象
         */
        int i1 = RandomUtil.randomInt();
        System.out.println(i1); //从int的范围内随机取数
        int i2 = RandomUtil.randomInt(10);//给定范围内随机取数
        System.out.println(i2);
        RandomUtil.randomEles(StrList, 6).forEach(System.out::println);//随机获得列表中的元素(有重复数据)
        System.out.println("----------randomEle-----------");
        RandomUtil.randomEleSet(StrList, 6).forEach(System.out::println);//随机获得列表中的一定量的不重复元素，返回Set(无重复数据)
        System.out.println("------------randomString---------");
        System.out.println(RandomUtil.randomString(6));// 获得一个随机的字符串(包含字母和数字)
        System.out.println(RandomUtil.randomString("abcdefghijklmnopqrstuvwxyz", 6));//有重复的字母
        System.out.println(RandomUtil.randomStringUpper(6));//转大写之后的字符串
        /**
         * HashMap扩展-Dict
         */
        System.out.println("------------Dict---------");
        Dict dict = Dict.create();
        dict.set("key1", 1)//int
            .set("key2", 1000L)//long
            .set("key3", DateTime.now());//Date

        System.out.println(dict.get("key1"));
        System.out.println(dict.get("key2"));
        System.out.println(dict.get("key3"));
        System.out.println(dict.getLong("key2"));
        for (Map.Entry<String, Object> entry : dict.entrySet()) {
            System.out.println(entry.getKey() +":"+ entry.getValue());
        }
        System.out.println("----------Validator-----------");//可以验证很多东西
//        Validator.validateChinese("我是一段zhongwen", "内容中包含非中文");
        Validator.validateMobile("15882346334","手机号格式不正确");

    }

    private static List<String> getRandomList(List<String> initialList, int size) {
        if (initialList.size() <= size) return initialList;
        Integer[] random = NumberUtil.generateBySet(0, initialList.size(), size);
        System.out.println(Arrays.toString(random));
        List<String> result = new ArrayList<>();
        for (Integer r : random) {
            result.add(initialList.get(r));
        }
        return result;

    }
}
