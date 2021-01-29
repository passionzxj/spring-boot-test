package com.zhang.jwt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.jwt.domain.Department;
import com.zhang.jwt.mapper.DepartmentMapper;
import com.zhang.jwt.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public Map<String, List<Department>> group(){
        List<Department> departments = this.getBaseMapper().selectList(null);
        Map<String, List<Department>> listMap = departments.stream()
                .collect(Collectors.groupingBy(Department::getCampusId));
        Map<String, List<Department>> map = new HashMap<>();
        for (Map.Entry<String, List<Department>> entry : listMap.entrySet()) {
            String[] stringList = entry.getKey().split(",");
            for (String campusId : stringList) {
                List<Department> departmentList = map.get(campusId);
                if (CollectionUtil.isNotEmpty(departmentList)) {
                    List<Department> departments1 = new ArrayList<>(departmentList);
                    departments1.addAll(entry.getValue());
                    map.put(campusId,departments1);
                }else{
                    map.put(campusId,entry.getValue());
                }
            }
        }
        return map;
    }
}
