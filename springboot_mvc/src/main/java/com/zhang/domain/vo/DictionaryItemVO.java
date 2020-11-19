package com.zhang.domain.vo;

import com.zhang.domain.DictionaryItem;
import com.zhang.domain.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DictionaryItemVO extends DictionaryItem {
    private String dictName;
    private String subjectName;

}
