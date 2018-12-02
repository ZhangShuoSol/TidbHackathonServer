package com.tidbhack.backend.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenbinsong on 2018/11/30.
 */
public class TableIndex {
    @ApiModelProperty(value = "表名")
    private String table;
    @ApiModelProperty(value = "索引名")
    private String columns;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }
}
