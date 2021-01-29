package com.zhang.jwt.domain.vo;

import com.zhang.jwt.domain.DictionaryItem;
import lombok.Data;

@Data
public class DictionaryItemVO extends DictionaryItem {
    private String dictName;
    private String subjectName;

}
