package com.zhang.jwt.hutool;

import lombok.Data;

import lombok.Data;

import java.util.List;
@Data
public class TreeNode {
    /**
     * 节点id
     */
    private String id;
    /**
     * 父节点 默认0为根节点
     */
    private String parentId;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 是否有子节点
     */
    private boolean hasChild;
 
    public TreeNode(String id, String parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }
}