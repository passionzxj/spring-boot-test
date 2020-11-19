package com.code.gen.out.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

public class CodeGen {
    public static void main(String[] args) {
        AutoGenerator generator = new AutoGenerator();
        //1.全局配置
        GlobalConfig gc = new GlobalConfig();
        String property = System.getProperty("user.dir");
//        gc.setOutputDir(property+"/src/main/java");
        gc.setOutputDir(property+"/code_gen/src/main/java");
        gc.setOpen(false);
        gc.setFileOverride(false);
        gc.setAuthor("author");
        gc.setServiceName("%sService");
        gc.setIdType(IdType.ASSIGN_ID);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true);
        generator.setGlobalConfig(gc);

        //2.设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl("jdbc:mysql://localhost:3306/zhang");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        generator.setDataSource(dsc);

        //3.包配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName("xin");
        pc.setParent("com.code.gen.out");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("mapper");
        pc.setController("web.controller");
        generator.setPackageInfo(pc);

        //4.策略配置
        StrategyConfig strategy = new StrategyConfig();
        //下划线转驼峰命名
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setInclude("order_head");
        strategy.setLogicDeleteFieldName("delete");
        TableFill create = new TableFill("create_time", FieldFill.INSERT);
        TableFill update = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        List<TableFill> list = new ArrayList<>();
        list.add(create);
        list.add(update);
        strategy.setTableFillList(list);
        generator.setStrategy(strategy);
        //乐观锁
        strategy.setVersionFieldName("version");
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);//localhost:8080/hello_id_1

        generator.setStrategy(strategy);
        generator.execute();
    }
}
