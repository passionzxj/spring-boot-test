package com.zhang.jwt;

import com.zhang.jwt.domain.Ability;
import com.zhang.jwt.service.IAbilityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AbilityTest {

    @Autowired
    private IAbilityService abilityService;

    @Test
    public void test() throws Exception{
        List<Ability> listTree = abilityService.findListTree(0L);
        for (Ability ability : listTree) {
            System.out.println(ability);
            for (Ability child : ability.getChildren()) {
                System.out.println(child);
            }
            System.out.println("==================");
        }
    }

    @Test
    public void testAdd() throws Exception{
        Ability ability = new Ability();
        ability.setName("说明文");
        ability.setPid(4L);
        ability.setDescription("说明文,说明什么");
        ability.setCreateTime(LocalDateTime.now());
        abilityService.save(ability);
    }

    @Test
    public void testDel() throws Exception{
        abilityService.removeAllById("6");
    }
}
