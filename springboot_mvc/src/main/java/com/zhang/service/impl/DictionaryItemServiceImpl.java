package com.zhang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.domain.Dictionary;
import com.zhang.domain.DictionaryItem;
import com.zhang.mapper.DictionaryItemMapper;
import com.zhang.mapper.DictionaryMapper;
import com.zhang.service.IDictionaryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DictionaryItemServiceImpl extends ServiceImpl<DictionaryItemMapper,DictionaryItem> implements IDictionaryItemService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Autowired
    private DictionaryItemMapper itemMapper;

    @Override
    public List<DictionaryItem> findListBySn(String sn) {

        Dictionary dictionary = dictionaryMapper.selectOne(new QueryWrapper<Dictionary>()
                .eq("sn", sn));
        List<DictionaryItem> dictionaryItemList = itemMapper.selectList(new QueryWrapper<DictionaryItem>()
                .eq("dict_id", dictionary.getId()));
        return new ArrayList<>(dictionaryItemList);
    }
}
