package com.zhang.jwt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.jwt.domain.Department;

import java.util.List;
import java.util.Map;

public interface IDepartmentService extends IService<Department> {
    Map<String, Object> findDept();
    List getDept();
    Map<String, List<Department>> group();

}
