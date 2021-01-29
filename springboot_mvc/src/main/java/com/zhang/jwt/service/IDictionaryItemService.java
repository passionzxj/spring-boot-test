package com.zhang.jwt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.jwt.domain.DictionaryItem;

import java.util.List;

public interface IDictionaryItemService extends IService<DictionaryItem> {

    /**
     * 根据sn查出所以的明细
     *
     * @param sn
     * @return
     */
    List<DictionaryItem> findListBySn(String sn);
}
