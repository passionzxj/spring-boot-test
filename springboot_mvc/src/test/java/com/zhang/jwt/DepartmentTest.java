package com.zhang.jwt;

import com.zhang.jwt.domain.Department;
import com.zhang.jwt.service.IDepartmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentTest {

    @Autowired
    private IDepartmentService departmentService;


    @Test
    public void test() throws Exception{
        Map<String, List<Department>> group = departmentService.group();
        System.out.println();
    }
}
