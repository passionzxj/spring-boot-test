package com.zhang.jwt.web.controller;

import com.zhang.jwt.domain.Department;
import com.zhang.jwt.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dept")
@CrossOrigin
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @GetMapping
    public Map<String,Object> getDepartmentList(){
        Map<String, Object> result = new HashMap<>();
        try {
            List<Department> departmentList = departmentService.list();
            result.put("success", true);
            result.put("data", departmentList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            return result;
        }
    }


    @GetMapping("map")
    public ResponseEntity getDept(String id){
        System.out.println(id);
        Map<String, Object> dept = departmentService.findDept();
//        List dept = departmentService.getDept();
        return ResponseEntity.ok(dept);
    }

}
