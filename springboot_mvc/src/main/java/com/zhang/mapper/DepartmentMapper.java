package com.zhang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.domain.Department;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface DepartmentMapper extends BaseMapper<Department> {

    @MapKey("name")
    Map<String, Object> selectDept();
    List<Map<String, Object>> selectDepts();
}
