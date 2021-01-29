package com.zhang.jwt.web.controller;


import com.zhang.jwt.domain.DictionaryItem;
import com.zhang.jwt.domain.vo.DictionaryItemVO;
import com.zhang.jwt.service.IDictionaryItemService;
import com.zhang.jwt.service.IDictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dictionary")
//@Slf4j
public class DictionaryItemController {
    private Logger logger = LoggerFactory.getLogger(DictionaryItemController.class);

    @Autowired
    private IDictionaryItemService itemService;
    @Autowired
    private IDictionaryService dictionaryService;


    @GetMapping("/item")
    public List<DictionaryItemVO> getListBySn(@RequestParam String sn) {
        try {
            List<DictionaryItemVO> result = new ArrayList<>();
            List<DictionaryItem> dictionaryItemList = itemService.findListBySn(sn);
            for (DictionaryItem dictionaryItem : dictionaryItemList) {
                DictionaryItemVO vo = new DictionaryItemVO();
                BeanUtils.copyProperties(dictionaryItem, vo);
                vo.setDictName(dictionaryService.getById(dictionaryItem.getDictId()).getName());
                result.add(vo);
            }
            return result;
        } catch (Exception e) {
//            e.printStackTrace();
            this.logger.error(e.getMessage());
            return null;
        }
    }
}
