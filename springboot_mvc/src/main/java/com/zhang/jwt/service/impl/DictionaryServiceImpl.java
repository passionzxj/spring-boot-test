package com.zhang.jwt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.jwt.domain.Dictionary;
import com.zhang.jwt.mapper.DictionaryMapper;
import com.zhang.jwt.service.IDictionaryService;
import org.springframework.stereotype.Service;


@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements IDictionaryService {
}
