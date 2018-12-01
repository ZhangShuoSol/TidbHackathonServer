package com.tidbhack.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tidbhack.backend.utils.FormatStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public class Node {
    private String name;
    private Integer layer;
    private List<Node> nodes;
    private Node parent;
    private Info info;
    private String uuid;

    public Node(String uuid, String name, String task, String count, String operatorinfo, String executeinfo) {
        this.uuid = uuid;
        this.layer = getRowLayer(name);
        this.name = FormatStringUtils.formatJsonName(name);
        this.info = new Info();
        this.info.setCount(count);
        this.info.setTask(task);
        this.info.setOperatorinfo(operatorinfo);
        this.info.setExecuteinfo(executeinfo);
        this.nodes = new ArrayList<Node>();
    }

    public Integer getRowLayer(String name) {
        Integer total = 0;
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (ch < 'A' || ch > 'Z') {
                total++;
            } else {
                break;
            }
        }

        return total / 2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    @JsonIgnore
    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
