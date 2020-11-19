package com.zhang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.domain.Department;

import java.util.List;
import java.util.Map;

public interface IDepartmentService extends IService<Department> {
    Map<String, Object> findDept();
    List getDept();
}
