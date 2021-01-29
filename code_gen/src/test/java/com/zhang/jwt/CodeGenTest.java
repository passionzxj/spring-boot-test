package com.zhang.jwt;

import com.code.gen.out.CodeGenApplication;
import com.code.gen.out.service.OrderHeadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodeGenApplication.class)
public class CodeGenTest {

    @Autowired
    private OrderHeadService orderHeadService;

    @Test
    public void testInsert() throws Exception{}

}
