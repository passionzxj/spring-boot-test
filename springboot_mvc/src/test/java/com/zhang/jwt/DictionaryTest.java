package com.zhang.jwt;


import com.zhang.jwt.domain.DictionaryItem;
import com.zhang.jwt.service.IDictionaryItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DictionaryTest {

    @Autowired
    private IDictionaryItemService itemService;

    @Test
    public void testFindListBySn() throws Exception{
        List<DictionaryItem> dictionaryItemList = itemService.findListBySn("111");
        for (DictionaryItem dictionaryItem : dictionaryItemList) {
            System.out.println(dictionaryItem);
        }

    }
}
