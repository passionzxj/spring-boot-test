package com.zhang.jwt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.jwt.domain.Ability;

import java.util.List;

public interface IAbilityService extends IService<Ability> {

    /**
     * 根据pid获得知识点的菜单树
     *
     * @param pid
     * @return
     */
    List<Ability> findListTree(Long pid);

    /**
     * 根据pid删除所有的子分类
     *
     * @param abilityId
     */
    void removeAllById(String abilityId);

    List<Ability> findTree(String abilityId);
}
