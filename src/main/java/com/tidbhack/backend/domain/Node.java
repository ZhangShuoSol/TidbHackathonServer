package com.tidbhack.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.tidbhack.backend.utils.FormatStringUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String color;

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
        //添加格式化后的info信息
        this.info.setExecuteinfoMap(formatStringToMap(executeinfo));
        //this.info.setOperatorinfoMap(formatStringToMap(formatOperatorinfoToJson(operatorinfo)));
        this.info.setOperatorinfoMap(null);
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

    /**
     * 格式化info信息为Map
     * @param info
     * @return
     */
    private Map formatStringToMap(String info) {
        Map map = new HashMap();
        if (!StringUtils.isEmpty(info)) {
            StringBuilder temp = new StringBuilder();
            temp.append("{");
            temp.append(info.substring(0, info.length()));
            temp.append("}");
            Gson gson = new Gson();
            map = gson.fromJson(temp.toString(), Map.class);
        }
        return map;
    }

    /**
     * 格式化操作信息为Json
     * @param operatorinfo
     * @return
     */
    private String formatOperatorinfoToJson(String operatorinfo){
        StringBuilder sb = new StringBuilder();
        if(!StringUtils.isEmpty(operatorinfo)){
            if(operatorinfo.indexOf(",")>-1){
                String temp = operatorinfo.substring(0,operatorinfo.indexOf(","));
                if(temp.indexOf(":")<0){
                    temp = "operate:"+temp+",";
                    sb.append(temp);
                    sb.append(operatorinfo.substring(operatorinfo.indexOf(",")+1,operatorinfo.length()));
                }else{
                    sb.append(operatorinfo);
                }
            }else{
                sb.append(operatorinfo);
            }

        }
        return sb.toString().replace(" ","");
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
