package com.zhang.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//Spring boot方式
@EnableTransactionManagement
@Configuration
@MapperScan("com.zhang.mapper")
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        return paginationInterceptor;
    }

    /**
     * 数据库中的id在传回前台时都使用string转化
     * @param builder
     * @return
     */
//    /**
//     * 方式一：此方式可以灵活配置任意类型的序列化反序列化
//     */
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer builderCustomizer() {
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        DateTimeFormatter dateTimeSerializeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        DateTimeFormatter dateTimeDeserializeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        return builder -> {
//            // 所有Long类型转换成String到前台
//            builder.serializerByType(Long.class, ToStringSerializer.instance);
//            builder.serializerByType(LocalDate.class, new LocalDateSerializer(dateFormatter));
//            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeSerializeFormatter));
//            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeDeserializeFormatter));
//        };
//    }

    /**
     * 方式二：采用objectMapper注入
     */
//    @Bean
//    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
//        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//        SimpleModule simpleModule = new SimpleModule();
//        // 直接将所有的Long类型转换为String
//        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
//        objectMapper.registerModule(simpleModule);
//        return objectMapper;
//    }
}

