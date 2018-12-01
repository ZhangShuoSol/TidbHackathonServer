package com.tidbhack.backend.domain;

import javax.sound.sampled.Line;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public class Info extends InfoMap{
    private String task;
    private String count;
    private String operatorinfo;
    private String executeinfo;

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
