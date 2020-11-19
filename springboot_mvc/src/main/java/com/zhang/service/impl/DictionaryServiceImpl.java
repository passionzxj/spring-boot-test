package com.zhang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.domain.Dictionary;
import com.zhang.mapper.DictionaryMapper;
import com.zhang.service.IDictionaryService;
import org.springframework.stereotype.Service;


@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements IDictionaryService {
}
