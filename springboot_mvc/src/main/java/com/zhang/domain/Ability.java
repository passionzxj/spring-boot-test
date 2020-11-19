package com.zhang.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@TableName("t_ability")
public class Ability {

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    private String name;

    private Long pid;

    private String description;

    private Integer sortIndex;

    @TableField(exist = false)
    private List<Ability> children = new ArrayList<>();
}
