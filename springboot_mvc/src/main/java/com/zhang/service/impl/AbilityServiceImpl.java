package com.zhang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.domain.Ability;
import com.zhang.hutool.tree.AbilityDO;
import com.zhang.hutool.tree.TreeUtil;
import com.zhang.mapper.AbilityMapper;
import com.zhang.service.IAbilityService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AbilityServiceImpl extends ServiceImpl<AbilityMapper, Ability> implements IAbilityService {

    @Autowired
    private AbilityMapper abilityMapper;

    @Autowired
    private MapperFacade mf;

    @Override
    public List<Ability> findListTree(Long pid) {
        List<Ability> result = new ArrayList<>();
        List<Ability> allAbility = abilityMapper.selectList(null);
        Map<String, Ability> map = new HashMap<>();
        for (Ability ability : allAbility) {
            map.put(ability.getId(), ability);
        }
        for (Ability ability : allAbility) {
            Long pidTem = ability.getPid();//临时pid
            if (pidTem.longValue() == pid.longValue()) {
                result.add(ability);
            } else {
                Ability parent = map.get(String.valueOf(pidTem));
                if (parent != null)
                    parent.getChildren().add(ability);
            }
        }
        return result;
    }

    @Override
    public List<Ability> findTree(String abilityId) {
        List<Ability> result = new ArrayList<>();
        List<Ability> allAbility = abilityMapper.selectList(new QueryWrapper<>());
        Map<String, Ability> map = new HashMap<>();
        for (Ability ability : allAbility) {
            map.put(ability.getId(), ability);
        }
        for (Ability item : allAbility) {
            Long pidTem = item.getPid();
            if (item.getId().equals(abilityId)) {
                result.add(item);
            } else {
                Ability parent = map.get(String.valueOf(pidTem));
                if (parent != null)
                    parent.getChildren().add(item);
            }
        }
        return result;
    }

    @Override
    public void removeAllById(String abilityId) {
        // 根据传入的id找出所有的子级节点
        List<Ability> childrenList = abilityMapper.selectList(new QueryWrapper<Ability>()
                .eq("pid", abilityId));
        if (!CollectionUtils.isEmpty(childrenList)) {
            List<String> ids = childrenList.stream().map(Ability::getId).collect(Collectors.toList());
            for (String id : ids) {
                abilityMapper.deleteById(id);
                removeAllById(id);
            }
        }
        abilityMapper.deleteById(abilityId);
    }

    public void getTree(){
        List<Ability> abilities = abilityMapper.selectList(null);
        List<AbilityDO> dos = mf.mapAsList(abilities, AbilityDO.class);
        List<AbilityDO> bulid = TreeUtil.bulid(dos, 0);

    }


}
