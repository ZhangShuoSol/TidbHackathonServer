package com.tidbhack.backend.datasource;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public class Explain {
    private String id;
    private String count;
    private String task;
    private String operator_info;
    private String execution_info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getOperator_info() {
        return operator_info;
    }

    public void setOperator_info(String operator_info) {
        this.operator_info = operator_info;
    }

    public String getExecution_info() {
        return execution_info;
    }

    public void setExecution_info(String execution_info) {
        this.execution_info = execution_info;
    }
}
