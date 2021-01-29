package com.zhang.jwt.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_dictionary_item")
public class DictionaryItem {

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    private String name;
    private Long dictId;
    private String intro;
}
