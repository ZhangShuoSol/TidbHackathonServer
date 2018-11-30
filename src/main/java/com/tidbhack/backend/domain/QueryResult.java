package com.tidbhack.backend.domain;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public class QueryResult {
    private String id;
    private String task;
    private String count;
    private String operatorinfo;
    private String executeinfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOperatorinfo() {
        return operatorinfo;
    }

    public void setOperatorinfo(String operatorinfo) {
        this.operatorinfo = operatorinfo;
    }

    public String getExecuteinfo() {
        return executeinfo;
    }

    public void setExecuteinfo(String executeinfo) {
        this.executeinfo = executeinfo;
    }
}
