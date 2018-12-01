package com.tidbhack.backend.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public class Statement {
    @ApiModelProperty(value = "select sql 语句")
    private String sql;

    @ApiModelProperty(value = "select 当前的UUID")
    private String id;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
