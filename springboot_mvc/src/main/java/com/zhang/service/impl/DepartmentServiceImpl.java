package com.zhang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.domain.Department;
import com.zhang.mapper.DepartmentMapper;
import com.zhang.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public Map<String, Object> findDept() {
        return departmentMapper.selectDept();
    }


    public List getDept() {
        List<Map<String, Object>> list = departmentMapper.selectDepts();
        return list;
    }
}
